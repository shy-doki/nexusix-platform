package com.shy.nexusix.iam.service;

import com.shy.nexusix.common.result.ApiResponse;
import com.shy.nexusix.iam.rto.LoginRTO;

public interface IAuthService {

    ApiResponse login(LoginRTO param);

}
