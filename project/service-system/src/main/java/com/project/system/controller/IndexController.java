package com.project.system.controller;

import com.project.common.result.Result;
import com.project.common.utils.JwtHelper;
import com.project.common.utils.MD5;
import com.project.model.system.SysUser;
import com.project.model.vo.LoginVo;
import com.project.system.exception.CustomException;
import com.project.system.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lingjun
 * @date 2023/4/7 22:34
 */

@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;
    //login
    //{"code":20000,"data":{"token":"admin-token"}}
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        //Query data according to username
        SysUser sysUser = sysUserService.getUserInfoByUserName(loginVo.getUsername());

        //If the query is null
        if(sysUser == null) {
            throw new CustomException(20001,"User does not exist");
        }

        //Determine if the password is the same
        String password = loginVo.getPassword();
        String md5Password = MD5.encrypt(password);
        if(!sysUser.getPassword().equals(md5Password)) {
        throw new CustomException(20001,"Incorrect password");
        }

        //Determine if a user is available
        if(sysUser.getStatus().intValue()==0) {
            throw new CustomException(20001,"The user has been disabled");
        }

        //Generate a token string based on userid and username and return it via map
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    //info
    //{"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator",
    // "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        //Get the value named "token" from the header of the HTTP request
        String token = request.getHeader("token");

        //Get user name from token string（id）
        String username = JwtHelper.getUsername(token);

        //Get user information (basic information and menu permission and button permission data) based on username
        Map<String,Object> map = sysUserService.getUserInfo(username);
        return Result.ok(map);
    }

//    @PostMapping("logout")
//    public Result logout(){
//        return Result.ok();
//    }

}
