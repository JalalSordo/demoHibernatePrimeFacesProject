/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.controller.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.sample.model.dao.UserDAO;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.sample.controller.utils.SessionUtils;
import org.sample.model.entities.User;
import org.primefaces.model.UploadedFile;
import org.sample.controller.filters.ImagesServlet;

/**
 *
 */
@ManagedBean(name = "userProfileBean")
@SessionScoped
public class UserProfileBean implements Serializable {

    private User connectedUser;

    private UserDAO userDAO;
    
    private Date date1;

    
    
    @PostConstruct
    public void init() {
        userDAO = new UserDAO();
        date1 = new Date();
        connectedUser = SessionUtils.getConnectedUser();
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }
    
    public void saveUser() {
        
        //A modifier
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        connectedUser.setDob(formatter.format(date1));
        
        userDAO.saveUser(connectedUser);
        SessionUtils.setConnectedUser(connectedUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Les informations de l'utilisateur ont été modifiées avec succès !"));
    }

    public void saveUserProfilePicture() throws IOException {

        userDAO.saveUser(connectedUser);
        SessionUtils.setConnectedUser(connectedUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Les informations de l'utilisateur ont été modifiées avec succès !"));
    }

    public void uploadProfileImage(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        connectedUser.setPictureRef(fileName);
        SessionUtils.setConnectedUser(connectedUser);
        saveFileInFileSystem(uploadedFile);
    }

    public void uploadUserAttachment(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        connectedUser.setDocumentRef(fileName);
        SessionUtils.setConnectedUser(connectedUser);
        saveFileInFileSystem(uploadedFile);
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    private void saveFileInFileSystem(UploadedFile uploadedImage) throws IOException {

        OutputStream out;
        try (InputStream is = uploadedImage.getInputstream()) {
            out = new FileOutputStream(ImagesServlet.IMAGE_STORE + uploadedImage.getFileName());
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
        out.close();
    }

    public void downloadUserAttachemnt() throws Exception {
        File file = new File(ImagesServlet.IMAGE_STORE + connectedUser.getDocumentRef());
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        //ec.setResponseContentType(file.); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
        ec.setResponseContentLength((int) file.length()); // Set it with the file size. This header is optional. It will work if it's omitted, but the download progress will be unknown.
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\""); // The Save As popup magic is done here. You can give it any file name you want, this only won't work in MSIE, it will use current request URL as file name instead.

        OutputStream output = ec.getResponseOutputStream();
        RandomAccessFile ra = new RandomAccessFile(file, "rw");
        byte[] fileData = new byte[(int) file.length()];
        try {
            ra.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.write(fileData);
        fc.responseComplete();

    }

}
