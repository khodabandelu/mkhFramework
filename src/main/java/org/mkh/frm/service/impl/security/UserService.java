package org.mkh.frm.service.impl.security;

import org.mkh.frm.common.exception.ApplicationException;
import org.mkh.frm.dao.IGenericRepository;
import org.mkh.frm.dao.security.IUserRepository;
import org.mkh.frm.domain.security.Group;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.security.SecurityUtility;
import org.mkh.frm.service.impl.GenericService;
import org.mkh.frm.service.security.IGroupService;
import org.mkh.frm.service.security.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends GenericService<User, Integer> implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IGroupService groupService;


    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    protected IGenericRepository<User, Integer> getGenericRepo() {
        return userRepository;
    }

    @Override
    @Transactional
    public int updateLastUserVisitDate(Date lastVisitDate) {
        return userRepository.updateLastUserVisitDate(lastVisitDate, SecurityUtility.getAuthenticatedUserId());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public Boolean saveGroup(int userId, List<Group> groups) {
        if (SecurityUtility.getAuthenticatedUserId().equals(userId))
            throw new ApplicationException("you can not change your group access");

        return userRepository.saveGroup(userId, groups);
    }


    @Override
    @Transactional
    public boolean changePassword(String newPassword) {
        String encryptNewPassword = new BCryptPasswordEncoder().encode(newPassword);
        User user = SecurityUtility.getAuthenticatedUser();

        return userRepository.changePassword(user.getId(), encryptNewPassword) > 0;
    }


    @Override
    @Transactional
    public Integer save(User user) {
        user.setUsername(user.getUsername().toLowerCase());

        if (user.getId() == null || user.getId() < 0) {
            user.setEnabled(true);
            user.setCellphone(user.getUsername());

            validateUsername(user.getUsername());

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                int randomPass = new Random().nextInt((99999 - 10000) + 1) + 10000;
                user.setPasswordPlain(randomPass + "");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("eventName", "sendEmailAndSmsForCreateUser");
            params.put("user", user);
            params.put("password", user.getPassword());
            params.put("templateName", "CreateUser");
            publisher.publishEvent(user);

            user.setPassword(new BCryptPasswordEncoder().encode(user.getPasswordPlain()));

            super.save(user);

            if (user.getGroups() != null &&
                    user.getGroups().stream().filter(g -> g.getCode().equals("2")).count() == 0 && user.getGroups().size() == 0)
                addGroup(user.getId(), groupService.findByCode("2").getId());

            return user.getId();
        } else {
            return super.save(user);
        }
    }

    private void validateUsername(String username) {
        User user = findByUsername(username);
        if (user != null)
            //throw new ApplicationException("نام کاربری تکراری استu");
            throw new ApplicationException("username is exists and can not create user ");
    }


    @Override
    @Transactional
    public Boolean addGroup(int userId, int groupId) {
        return userRepository.addGroup(userId, groupId);
    }

    @Override
    public List<Group> getUserGroups(int userId) {
        User user = userRepository.loadByEntityId(userId);
        return new ArrayList<Group>(user.getGroups());
    }


    @Override
    @Transactional
    public void changeAvatar(Integer userId, String imageCode) {
        User user = loadByEntityId(userId);
        user.setAvatarImageCode(imageCode);
        super.save(user);
    }

    @Override
    public List<User> listByGroupId(Integer groupId) {
        return userRepository.listByGroupId(groupId);
    }

}
