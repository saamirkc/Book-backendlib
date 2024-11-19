# File Upload Functionality in Spring Boot

## Overview

This document provides a detailed explanation of the file upload process in our Spring Boot application, focusing on how uploaded files are managed, stored, and accessed.

## Setting Up the Directory Path

1. **Main Directory Configuration**:
    - The main directory where files will be stored is specified in the `fileUploadPath` property, configured in `application.properties` as follows:

      ```properties
      application.file.upload.photos-output-path = uploads
      ```

    - For each upload, the `saveFile` method generates a subdirectory path specific to the user, e.g., `uploads/users/userId`, where `userId` is the ID of the user uploading the file.

2. **Creating the Directory if It Doesn’t Exist**:
    - The `targetFolder` is a `File` object pointing to this specific user’s directory.
    - If this directory doesn’t already exist, the `mkdirs()` method is called to create it, ensuring a separate folder for each user to store their uploaded files.
    - If folder creation fails, a warning is logged, and `null` is returned.

3. **Handling File Extension and Naming**:
    - The `getFileExtension` method takes the file’s original name (using `sourceFile.getOriginalFilename()`) and extracts the extension, converting it to lowercase for consistency (e.g., `.JPG` becomes `.jpg`).
    - This extension is added to the final filename, which also includes `System.currentTimeMillis()` to make each filename unique. For example, the file path could look like `uploads/users/123/1632677123456.jpg`.

## Saving the File

- With `Paths.get(targetFilePath)`, the code converts the `targetFilePath` string into a `Path` object (`targetPath`), representing the complete path where the file will be stored.
- The `Files.write(targetPath, sourceFile.getBytes())` method writes the file data to this path, effectively saving it on the server.
- If the file is saved successfully, an info message is logged with the path. If there’s an error during file writing, it catches the `IOException`, logs an error, and returns `null`.

### Summary

Each uploaded file is saved under `uploads/users/userId` with a unique name based on the timestamp and original file extension. The method’s use of unique subdirectories and filenames ensures the files are organized and prevents overwriting when multiple files are uploaded.

## Example Configuration

**Configuration in `application.properties`**:

The base directory for uploads is set as follows:

```properties
application.file.upload.photos-output-path = uploads


This means all uploaded files will be stored in the uploads directory within your application’s root folder.
Example Scenario:

Suppose a user with userId = 123 uploads a file named profilePic.JPG.
Steps:

When the user uploads profilePic.JPG, here’s how it will be processed and saved:
finalUploadPath becomes: uploads/users/123
fileUploadSubPath for this user is created as: users/123
If the uploads/users/123 folder doesn’t exist, it will be created using mkdirs().
File Naming:

The method System.currentTimeMillis() is used to generate a unique filename based on the current time in milliseconds.
Assume System.currentTimeMillis() returns 1632677123456 for this example.
The final file extension extracted from profilePic.JPG is .jpg (converted to lowercase).
The final filename becomes 1632677123456.jpg.
Directory and File Path:

The file path uploads/users/123/1632677123456.jpg is created and saved.
Directory Structure After Upload
After the upload completes, the file directory structure in your project will look something like this:

 root_project_folder/
├── src/
├── uploads/
│   └── users/
│       └── 123/
│           └── 1632677123456.jpg
├── pom.xml
├── application.properties