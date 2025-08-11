package com.osp.bttp.common.contants;

import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.repository.db3.AuthoritiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author sangnk
 * @Created 09/10/2024 - 10:53 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Component
public class ConstantAuthor {
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    public interface Group { // nhóm quyền
        String author = "ROLE_SYSTEM_GROUP";
        String view = "ROLE_SYSTEM_GROUP_VIEW";
        String add = "ROLE_SYSTEM_GROUP_ADD";
        String edit = "ROLE_SYSTEM_GROUP_EDIT";
        String delete = "ROLE_SYSTEM_GROUP_DELETE";
        String user = "ROLE_SYSTEM_GROUP_USER";
    }
    public static class USER {
        public static final String view = "ROLE_USER_VIEW"; // Xem danh sách USER
        public static final String add = "ROLE_USER_ADD"; // Thêm mới danh sách USER
        public static final String detail = "ROLE_USER_VIEW_DETAIL"; // Xem chi tiết USER
        public static final String lock = "ROLE_USER_LOCK"; // Khóa/Mở khóa tài khoản
        public static final String recovery = "ROLE_USER_RECOVERY"; //  Khôi phục mật khẩu
        public static final String delete = "ROLE_USER_DELETE"; // Xóa tài khoản
        public static final String excel = "ROLE_USER_EXCEL"; // Xuất excel
        public static final String author = "ROLE_SYSTEM_USER_AUTHORITY"; // phân quyền
        public static final String updateUser = "ROLE_USER_UPDATE";
    }

    //quản trị hệ thống
    public static class SYSTEM {
        public static final String system = "ROLE_SYSTEM";
        public static final String report = "ROLE_REPORT";
    }

    //Nhập dữ liệu hoạt động HNCC, sửa dữ liệu đã nhập, xóa dữ liệu đã nhập, xem dữ liệu đã nhập, xuất dữ liệu đã nhập
    public static class NOTARY_ACTIVITY {
        public static final String author = "ROLE_NOTARY_ACTIVITY";
        public static final String view = "ROLE_NOTARY_ACTIVITY_VIEW";
        public static final String add = "ROLE_NOTARY_ACTIVITY_ADD";
        public static final String edit = "ROLE_NOTARY_ACTIVITY_EDIT";
        public static final String delete = "ROLE_NOTARY_ACTIVITY_DELETE";
        public static final String excel = "ROLE_NOTARY_ACTIVITY_EXCEL";
    }

    @Bean
    public void generate() {
        //check exits
        //notary activity
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.author).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.author);
            authority.setAuthority(NOTARY_ACTIVITY.author);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.view).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.view);
            authority.setAuthority(NOTARY_ACTIVITY.view);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.add).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.add);
            authority.setAuthority(NOTARY_ACTIVITY.add);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.edit).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.edit);
            authority.setAuthority(NOTARY_ACTIVITY.edit);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.delete).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.delete);
            authority.setAuthority(NOTARY_ACTIVITY.delete);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(NOTARY_ACTIVITY.excel).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(NOTARY_ACTIVITY.excel);
            authority.setAuthority(NOTARY_ACTIVITY.excel);
            authoritiesRepository.save(authority);
        }



        if (authoritiesRepository.findAllByAuthKey(SYSTEM.report).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(SYSTEM.report);
            authority.setAuthority(SYSTEM.report);
            authoritiesRepository.save(authority);
        }

        if (authoritiesRepository.findAllByAuthKey(Group.author).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.author);
            authority.setAuthority(Group.author);
            authoritiesRepository.save(authority);

        }
        if (authoritiesRepository.findAllByAuthKey(Group.view).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.view);
            authority.setAuthority(Group.view);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(Group.add).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.add);
            authority.setAuthority(Group.add);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(Group.edit).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.edit);
            authority.setAuthority(Group.edit);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(Group.delete).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.delete);
            authority.setAuthority(Group.delete);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(Group.user).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(Group.user);
            authority.setAuthority(Group.user);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.view).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.view);
            authority.setAuthority(USER.view);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.add).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.add);
            authority.setAuthority(USER.add);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.detail).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.detail);
            authority.setAuthority(USER.detail);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.lock).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.lock);
            authority.setAuthority(USER.lock);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.recovery).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.recovery);
            authority.setAuthority(USER.recovery);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.delete).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.delete);
            authority.setAuthority(USER.delete);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.excel).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.excel);
            authority.setAuthority(USER.excel);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.author).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.author);
            authority.setAuthority(USER.author);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(USER.updateUser).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(USER.updateUser);
            authority.setAuthority(USER.updateUser);
            authoritiesRepository.save(authority);
        }
        if (authoritiesRepository.findAllByAuthKey(SYSTEM.system).size() == 0) {
            Authority authority = new Authority();
            authority.setGenDate(new Date());
            authority.setLastUpdated(new Date());
            authority.setAuthKey(SYSTEM.system);
            authority.setAuthority(SYSTEM.system);
            authoritiesRepository.save(authority);
        }

    }
}
