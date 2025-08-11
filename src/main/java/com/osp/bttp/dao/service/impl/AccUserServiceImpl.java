package com.osp.bttp.dao.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.Result;
import com.osp.bttp.common.security.TokenHelper;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.StoreUtils;
import com.osp.bttp.dao.model.dto.AccessTokenInfo;
import com.osp.bttp.dao.model.dto.UserInfo;
import com.osp.bttp.dao.model.dto.db3.AccUserDTO;
import com.osp.bttp.dao.model.dto.request.AccountRegisterRequest;
import com.osp.bttp.dao.model.dto.request.UserLoginRequest;
import com.osp.bttp.dao.model.dto.response.AccountRegisterResponse;
import com.osp.bttp.dao.model.dto.response.UserInfoResponse;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.Group;
import com.osp.bttp.dao.model.entity.db3.GroupUser;
import com.osp.bttp.dao.model.mview.AccUserView;
import com.osp.bttp.dao.repository.db3.AccUserRepository;
import com.osp.bttp.dao.repository.db3.GroupUserRepository;
import com.osp.bttp.dao.service.AccUserService;
import com.osp.bttp.dao.service.GroupService;
import com.osp.bttp.dao.service.RedisService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author sangnk
 * @Created 08/10/2024 - 3:13 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AccUserServiceImpl implements AccUserService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private AccUserRepository accUserRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private StoreUtils storeUtils;


    @Override
    public ResponseEntity<ApiResponseV1<UserInfo>> authenUser(UserLoginRequest loginRequest, HttpServletRequest request) {
        try {
//            if(!checkCaptcha(loginRequest, request) ){
//                return new ResponseEntity<>(new ApiResponseV1<>(false, 24, "Mã xác nhận không đúng", null), HttpStatus.OK);
//            }
            AccUser user = accUserRepository.findByUsername(loginRequest.getUsername().trim());
            if (user == null) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 21, "Tài khoản chưa được đăng ký", null), HttpStatus.BAD_REQUEST);
            }
            if (user.getStatus() == 0) {
                String messageUserLock = "Tài khoản " + loginRequest.getUsername().trim() + " của bạn đã bị khóa!";
                return new ResponseEntity<>(new ApiResponseV1<>(false, 22, messageUserLock, null), HttpStatus.OK);
            }

            // Kiểm tra mật khẩu
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 23, "Mật khẩu không đúng", null), HttpStatus.OK);
            }

            List<String> listAuthor = groupService.loadListAuthorityByUsername(user.getUsername());

            // Trả về jwt cho người dùng.
            Map<String, Object> mapClaims = new HashMap<>();

            String jwt = tokenHelper.generateToken(loginRequest.getUsername().trim(), mapClaims);

            // update jwt user
            user.setJwt(jwt);

            accUserRepository.save(user);
            String refreshToken = tokenHelper.generateTokenRefreshToken(loginRequest.getUsername().trim());

            AccessTokenInfo accessToken = new AccessTokenInfo();
            accessToken.setAccessToken(jwt);
            accessToken.setRefreshToken(refreshToken);
            accessToken.setTokenType("Bearer");
            accessToken.setExpiresIn(tokenHelper.getClaimsFromToken(jwt).getExpiration().getTime());

            UserInfo userInfo = new UserInfo();
            userInfo.setAccessTokenInfo(accessToken);
            userInfo.setUserName(user.getUsername());

            redisService.reloadCacheUser(user.getId(), user.getUsername());


            userInfo.setAuthorities(listAuthor);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành Công", userInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponseV1<>(false, 5, "Đăng nhập thất bại", null));
        }
    }

    private boolean checkCaptcha(UserLoginRequest loginRequest, HttpServletRequest request) {
        var turnstileToken = loginRequest.getCf_turnstile_response();
        var secretKey = "0x4AAAAAAA5v6eMsx6JxpefEnsd1I_nf7oY";
        String url = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString("secret=" + secretKey + "&response=" + turnstileToken))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());

//            var check = jsonNode.get("success").asBoolean();
            return jsonNode.get("success").asBoolean();
        } catch (Exception e) {
            log.error("Captcha verification failed", e);
            return false;
        }

//        return true;
    }

    @Override
    public AccUser findById(Long userId) {
        return accUserRepository.findById(userId).orElse(null);
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> register(AccountRegisterRequest req, HttpServletRequest request) {
        try {
            AccountRegisterResponse registerResponse = new AccountRegisterResponse(req.getUsername());
            AccUser user = accUserRepository.findByUsername(req.getUsername());
            if (H.isTrue(user)) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 21, "Tài khoản đã tồn tại", null), HttpStatus.OK);
            } else {
                user = new AccUser();
                user.setUsername(req.getUsername());
                user.setPassword(passwordEncoder.encode(req.getPassword()));
                user.setFullName(req.getFullName());
                user.setStatus(1);
                user.setType(Constants.TYPE_USER.USER);

                user.setGenDate(new Date());
                user.setLastUpdated(new Date());
                user.setCreateBy("SYSTEM");
                user.setUpdateBy("SYSTEM");

                accUserRepository.save(user);

                registerResponse.setUsername(user.getUsername());
            }

            GroupUser groupUser = new GroupUser();
            groupUser.setUserId(user.getId());
            Group defaultgr = groupService.findByTypeAndIsDetault(user.getType(), 1L).orElse(null);
            if (H.isTrue(defaultgr)) {
                groupUser.setGroupId(defaultgr.getId());
                groupUserRepository.save(groupUser);
            }

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", registerResponse), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(Result.SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<UserInfoResponse>> getUserInfo() {
        try {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin == null) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 21, "Tài khoản chưa được đăng ký", null), HttpStatus.BAD_REQUEST);
            }
            List<String> listAuthor = groupService.loadListAuthorityByUsername(userLogin.getUsername());
            userInfoResponse.setListAuthority(listAuthor);
            userInfoResponse.setFullName(userLogin.getFullName());
            userInfoResponse.setUsername(userLogin.getUsername());
            userInfoResponse.setType(userLogin.getType());
            userInfoResponse.setAdministrationId(userLogin.getAdministrationId());
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", userInfoResponse), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponseV1<>(false, 5, "Đăng nhập thất bại", null));
        }
    }

    @Override
    public Optional<AccUser> getByUsername(String trim) {
        return accUserRepository.findByUsernameOpt(trim);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseV1 addUser(AccUserDTO item) {
        AccUser user = new AccUser();
        user = item.mapToEntity(user);
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(item.getPassword()));


        storeUtils.save(accUserRepository, user);


        GroupUser groupUser = new GroupUser();
        groupUser.setUserId(user.getId());
        Group defaultgr = groupService.findByTypeAndIsDetault(item.getType(), 1L).orElse(null);
        if (H.isTrue(defaultgr)) {
            groupUser.setGroupId(defaultgr.getId());
            storeUtils.save(groupUserRepository, groupUser);
        }

//            updateSourcePartner();
        return new ApiResponseV1(true, 0, "Thêm người dùng thành công", item);

    }

    @Override
    public ResponseEntity<ApiResponseV1> testRedis() {
        try {
            //show info redis
            redisService.showInfo();
            if ( redisEnable ) redisTemplate.opsForValue().set("test", "test", 10, TimeUnit.MINUTES);
            return ResponseEntity.ok(new ApiResponseV1(true, 0, "Thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new ApiResponseV1(false, 5, "Thất bại", null));
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> searchUser(int pageNumber, int numberPerPage, String username, String fullName, String type, String mobile, String email, String address) {
        int offset = 0;
        PagingResult page = new PagingResult();
        try {
            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            page.setPageNumber(pageNumber);
            page.setNumberPerPage(numberPerPage);
            String hql = "SELECT new com.osp.bttp.dao.model.mview.AccUserView(p.id, p.username, p.fullName, p.type, p.dateBirth, p.mobile, p.email, p.address, p.provinceId, p.administrationId) FROM AccUser p WHERE 1=1 ";
            if (H.isTrue(username)) {
                hql += " AND p.username LIKE :username ";
            }
            if (H.isTrue(fullName)) {
                hql += " AND p.fullName LIKE :fullName ";
            }
            if (H.isTrue(type)) {
                hql += " AND p.type = :type ";
            }
            if (H.isTrue(mobile)) {
                hql += " AND p.mobile LIKE :mobile ";
            }
            if (H.isTrue(email)) {
                hql += " AND p.email LIKE :email ";
            }
            if (H.isTrue(address)) {
                hql += " AND p.address LIKE :address ";
            }
            Query query = entityManager.createQuery(hql);
            if (H.isTrue(username)) {
                query.setParameter("username", "%" + username + "%");
            }
            if (H.isTrue(fullName)) {
                query.setParameter("fullName", "%" + fullName + "%");
            }
            if (H.isTrue(type)) {
                query.setParameter("type", type);
            }
            if (H.isTrue(mobile)) {
                query.setParameter("mobile", "%" + mobile + "%");
            }
            if (H.isTrue(email)) {
                query.setParameter("email", "%" + email + "%");
            }
            if (H.isTrue(address)) {
                query.setParameter("address", "%" + address + "%");
            }
            page.setRowCount(query.getResultList().size());
            query.setFirstResult(offset);
            query.setMaxResults(numberPerPage);
            List<AccUserView> lst = query.getResultList();
            page.setItems(lst);



        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", page), HttpStatus.OK);

    }

    public List<String> getLstAuthorityIsApp(String username) {
        try {
            String sql = "SELECT au.auth_key FROM acc_authorities au " +
                    " INNER JOIN acc_group_authorities ga ON ga.authority = au.id " +
                    " INNER JOIN acc_group_user gu ON gu.group_id = ga.group_id " +
                    " INNER JOIN acc_user p ON p.id = gu.user_id " +
                    " WHERE p.username = :username GROUP BY au.auth_key";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("username", username);
            List<String> list = query.getResultList();
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }


}
