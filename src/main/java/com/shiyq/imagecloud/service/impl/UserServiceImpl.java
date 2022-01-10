package com.shiyq.imagecloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyq.imagecloud.convert.UserConvert;
import com.shiyq.imagecloud.entity.DO.Setting;
import com.shiyq.imagecloud.entity.DO.User;
import com.shiyq.imagecloud.entity.DO.UserInfo;
import com.shiyq.imagecloud.entity.DTO.UserTokenDTO;
import com.shiyq.imagecloud.entity.VO.UserVO;
import com.shiyq.imagecloud.mapper.SettingMapper;
import com.shiyq.imagecloud.mapper.UserInfoMapper;
import com.shiyq.imagecloud.mapper.UserMapper;
import com.shiyq.imagecloud.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyq.imagecloud.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shiyq
 * @since 2022-01-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${spring.mail.username}")
    private String emailForm;

    private StringRedisTemplate stringRedisTemplate;
    private JavaMailSender mailSender;

    private UserMapper userMapper;
    private UserInfoMapper userInfoMapper;
    private SettingMapper settingMapper;


    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Autowired
    public void setSettingMapper(SettingMapper settingMapper) {
        this.settingMapper = settingMapper;
    }

    /**
     * 用户登录
     * @param userVO 用户登录验证信息
     * @return 成功返回用户信息，失败返回null
     */
    @Override
    public UserTokenDTO signIn(UserVO userVO) {
        // TODO 查库前是否进行再一次的数据校验
        // 设置检索条件（邮箱登录或id登录）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("password", userVO.getPassword());
        // 包含”@“，认为是邮箱登录，否则为id登录
        if (userVO.getUsername().contains("@"))
            queryWrapper.eq("username", userVO.getUsername());
        else
            queryWrapper.eq("id", Long.valueOf(userVO.getUsername()));
        // 查询数据库
        User user = userMapper.selectOne(queryWrapper);
        // 未检索到则返回null，否则生成token返回前端
        return user == null ? null : UserConvert.INSTANCE.userDOToDTO(user);
    }

    /**
     * 用户注册
     * @param userVO 用户注册信息
     * @return 成功返回用户信息，失败返回null
     */
    @Override
    @Transactional
    public UserTokenDTO signup(UserVO userVO) {
        // 检索redis，验证邮箱验证码
        String code = stringRedisTemplate.opsForValue().get(userVO.getUsername());
        // 检索不到key或验证码错误，均返回空
        if (code == null || !code.equals(userVO.getCode()))
            return null;
        User user = UserConvert.INSTANCE.userVOtoDO(userVO);
        // 插入用户（事务）
        userMapper.insert(user);
        userInfoMapper.insert(new UserInfo(user.getId(), "[]"));
        settingMapper.insert(new Setting(user.getId()));
        // 3张表均插入成功后，即注册成功，删除redis验证码缓存
        stringRedisTemplate.delete(userVO.getUsername());
        // 生成并返回token
        return UserConvert.INSTANCE.userDOToDTO(user);
    }

    /**
     * 检查用户名（邮箱地址）是否已存在
     */
    @Override
    public boolean checkSameUsername(String username) {
        return userMapper.checkSameUsername(username);
    }

    /**
     * 发送邮箱验证码
     * @param emailTo 接收者邮箱
     * @return 是否成功
     */
    @Override
    public boolean sendEmailVerificationCode(String emailTo) {
        // 生成验证码（后6位为验证码）
        String code = String.valueOf(new Date().getTime()).substring(7);
        // 存入redis，10分钟后失效
        stringRedisTemplate.opsForValue().set(emailTo, code, 10, TimeUnit.MINUTES);
        // TODO 邮件发送，提出工具方法
        // TODO 定义邮件样式
        // TODO 邮件是否需要发送从主进程分离出来，异步发送？
        // 发送
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(emailForm);
        msg.setTo(emailTo);
        msg.setSubject("【Image Cloud】Email Address Signup Verification");
        msg.setText("Hi, welcome to Image Cloud. Your signup verification is  \""+code+"\" , " +
                "And it is valid for 10 minutes.");
        mailSender.send(msg);

        return true;
    }

}
