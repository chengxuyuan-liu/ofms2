package com.example.demo.service;

import com.example.demo.entity.UserInf;

public interface CheckPermissionsService {
    public Boolean checkUploadPremission(UserInf user);
    public Boolean checkDownloadPremission(UserInf userInf);
    public Boolean checkPreviewPremission(UserInf userInf);
}
