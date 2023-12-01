package com.poluectov.criticine.webapp.controller.createcinemapage;

import com.poluectov.criticine.webapp.ApplicationContext;
import com.poluectov.criticine.webapp.controller.ErrorMessage;
import com.poluectov.criticine.webapp.controller.HttpMethodEnum;
import com.poluectov.criticine.webapp.controller.ServletCommand;
import com.poluectov.criticine.webapp.exception.DataBaseNotAvailableException;
import com.poluectov.criticine.webapp.model.data.Cinema;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static com.poluectov.criticine.webapp.controller.HttpMethodEnum.POST;
import static com.poluectov.criticine.webapp.controller.HttpMethodEnum.PUT;

public class PostCreateCinemaCommand implements ServletCommand {

    private static Logger logger = Logger.getLogger(PostCreateCinemaCommand.class.getName());

    HttpMethodEnum method;

    public PostCreateCinemaCommand(HttpMethodEnum method){
        this.method = method;
    }

    public PostCreateCinemaCommand() {
        method = POST;
    }

    @Override
    public void execute(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) resp;

        List<ErrorMessage> errors = new ArrayList<>();

        String uploadPath = request.getServletContext().getRealPath("") + ApplicationContext.UPLOAD_DIRECTORY;

        //saving picture file

        // creates the save directory if it does not exist
        Path uploadDir = Path.of(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String pictureFileName = ApplicationContext.DEFAULT_CINEMA_PICTURE_FILE_NAME;
        Part filePart = request.getPart("picture"); // Retrieves <input type="file" name="file">

        String cinemaIdStr = request.getParameter("cinema-id");
        int cinemaId = -1;
        if (cinemaIdStr != null) {
            try{
                cinemaId = Integer.parseInt(cinemaIdStr);
            }catch (NumberFormatException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.VALIDATION_ERROR));
            }
        }
        Cinema existingCinema = null;
        if (cinemaId != -1){
            try{
                existingCinema = ApplicationContext.INSTANCE.getCinemaDAO().get(cinemaId);
            }catch (DataBaseNotAvailableException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
            }catch (SQLException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
            }

            if (errors.isEmpty() && existingCinema == null){
                logger.error(ErrorMessage.VALIDATION_ERROR);
                errors.add(new ErrorMessage(ErrorMessage.VALIDATION_ERROR));
            }
        }

        if (filePart != null){
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // refines the fileName in case it is an absolute path
            if (!fileName.equals("")){
                fileName = sanitizeFileName(fileName);
                pictureFileName = generateUniqueFileName(fileName);
                String filePath = uploadPath + File.separator + pictureFileName;

                File file = new File(filePath);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("uploaded in " + file.getAbsolutePath());
                }catch (IOException e){
                    logger.error(e);
                    pictureFileName = ApplicationContext.DEFAULT_CINEMA_PICTURE_FILE_NAME;
                    errors.add(new ErrorMessage(ErrorMessage.PICTURE_ERROR));
                }
            }else {
                if (existingCinema != null){
                    pictureFileName = existingCinema.getPictureFile();
                }
            }
        }
        float rating = 0.0f;
        if (existingCinema != null){
            rating = existingCinema.getRating();
        }

        String cinemaName = request.getParameter("cinema-name");
        String year = request.getParameter("creation-year");
        //TODO: validate year
        String cinemaType = request.getParameter("cinema-type");
        int cinemaTypeId = 1;
        try {
            cinemaTypeId = ApplicationContext.INSTANCE.getCinemaTypeDAO().get(Integer.parseInt(cinemaType)).getId();
        }catch (DataBaseNotAvailableException e){
            logger.error(e);
            errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
        }catch (SQLException e){
            logger.error(e);
            errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
        }catch (NumberFormatException e){
            logger.error(e);
            errors.add(new ErrorMessage(ErrorMessage.VALIDATION_ERROR));
        }

        int creationYear = validYear(year);
        if (creationYear == -1){
            errors.add(new ErrorMessage(ErrorMessage.YEAR_ERROR));
        }
        if (errors.isEmpty()){
            Cinema cinema = new Cinema(cinemaName, rating, creationYear, pictureFileName, cinemaTypeId);
            if (cinemaId != -1) cinema.setId(cinemaId);
            try{
                processCinema(cinema);
                System.out.println(cinema + " has been added");
            } catch (DataBaseNotAvailableException e){
                logger.error(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_CONNECTION));
            } catch (SQLException e){
                logger.info(e);
                errors.add(new ErrorMessage(ErrorMessage.DB_ERROR));
            }
        }

        if (!errors.isEmpty()){
            request.setAttribute("errors", errors);
            RequestDispatcher dispatcher = request.getRequestDispatcher(ApplicationContext.JSP_CREATE_MOVIE_PAGE);
            dispatcher.forward(request, response);
        }else{
            response.sendRedirect(ApplicationContext.DOMAIN_ADDRESS + ApplicationContext.MAIN_PAGE_ADDRESS);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    private String sanitizeFileName(String fileName) {
        // Remove special characters and spaces
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private String generateUniqueFileName(String fileName) {
        // Append a unique identifier to the file name
        String extension = "";
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            extension = fileName.substring(lastDotIndex);
            fileName = fileName.substring(0, lastDotIndex);
        }

        // Use UUID to generate a unique identifier
        String uniqueIdentifier = UUID.randomUUID().toString();

        // Concatenate the original file name, unique identifier, and extension
        return fileName + "_" + uniqueIdentifier + extension;
    }

    private int validYear(String year){
        int creationYear;
        try{
            creationYear = Integer.parseInt(year);
        }catch (NumberFormatException e){
            return -1;
        }
        if (creationYear < 1800  || creationYear > 2100){
            return -1;
        }
        return creationYear;
    }


    private void processCinema(Cinema cinema) throws SQLException{
        if (method.equals(POST)){
            ApplicationContext.INSTANCE.getCinemaDAO().create(cinema);
        }else if (method.equals(PUT)){
            ApplicationContext.INSTANCE.getCinemaDAO().update(cinema.getId(), cinema);
        }
    }
}
