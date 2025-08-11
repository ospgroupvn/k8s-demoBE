package com.osp.bttp.endpoint;


import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.service.AccUserService;

import com.osp.bttp.dao.service.AuthorityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Admin on 1/9/2018.
 */
@RestController
@RequestMapping("/v1/api/authority")
@Secured({ConstantAuthor.SYSTEM.system})
public class AuthorityController {

    private Logger logger = LogManager.getLogger(AuthorityController.class);
//    @Autowired
//    private LogAccessDAO logAccessDao;
    @Autowired
    private AccUserService accUserService;
    @Autowired
    private AuthorityService authorityService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");



    @GetMapping("/search")
//    @Secured(ConstantAuthor.Authority.view)
    public ApiResponseV1<PagingResult> authorityList(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                                     @RequestParam(value = "numberPerPage", required = false, defaultValue = "15") int numberPerPage,
                                                     @RequestParam(value = "authKey", required = false, defaultValue = "") String authKey,
                                                     @RequestParam(value = "type", required = false, defaultValue = "") String type
    ) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        page.setNumberPerPage(numberPerPage);
        try {
            page = authorityService.page(page, authKey, type).orElse(new PagingResult());
        } catch (Exception e) {

        }
        return new ApiResponseV1<PagingResult>(true,"thành công",page);

    }

    @GetMapping("/get-list-auth-parent")
//    @Secured(ConstantAuthor.Authority.view)
    public ResponseEntity<List<Authority>> getListAuthparent(
            @RequestParam(value = "type", required = false,defaultValue = "") String type
    ) {
        Long authId = 0L;
        if (authId == null) {
            authId = 0L;
        }
        List<Authority> authoritys = new ArrayList<>();
        try {
            authoritys = authorityService.getListAuthParent(authId, type);
        } catch (Exception e) {

        }
        return new ResponseEntity<List<Authority>>(authoritys, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
//    @Secured(ConstantAuthor.Authority.add)
    public ResponseEntity<String> addAuthority(@RequestBody Authority authItem, HttpServletRequest request) {
        String page = "0";  // 0: no error, 1: error, 2: not required, 3 key exits
        try {
            if (!checkRequired(authItem)) {
                return new ResponseEntity<String>("2", HttpStatus.OK);
            } else if (authorityService.isExits(authItem)) {
                return new ResponseEntity<String>("3", HttpStatus.OK);
            } else {
                boolean isUpdate = authorityService.addAuthority(authItem);
                if (isUpdate) {
                    return new ResponseEntity<String>("0", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("1", HttpStatus.OK);

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("1", HttpStatus.OK);
        }
    }

    @PostMapping(value = "/edit")
//    @Secured(ConstantAuthor.Authority.edit)
    public ResponseEntity<String> editAuthority(@RequestBody @Valid Authority authItem, HttpServletRequest request) {String page = "0";  // 0: no error, 1: error, 2: not required, 3 not exits, 4 had auth child
        try {
            if (!checkRequired(authItem)) {
                return new ResponseEntity<String>("2", HttpStatus.OK);
//            } else if (authorityService.isExits(paramItem)) {
//                return new ResponseEntity<String>("3", HttpStatus.OK);
            } else {
                if (authItem == null || authItem.getId() == 0L) {
                    return new ResponseEntity<String>("3", HttpStatus.OK);
                }
                Authority authorityEdit = authorityService.getAuthorityById(authItem.getId());
                if (authorityEdit == null) {
                    return new ResponseEntity<String>("3", HttpStatus.OK);
                }

                if (!Objects.equals(authorityEdit.getFid(), authItem.getFid())) {
                    List<Authority> authorityChild = authorityService.getAuthorityChildrenById(authItem.getId());
                    if (authorityChild != null && !authorityChild.isEmpty() && authorityChild.size() > 0) {
                        return new ResponseEntity<String>("4", HttpStatus.OK);
                    }
                    authorityEdit.setFid(authItem.getFid());
                }

                authorityEdit.setDescription(authItem.getDescription());
                authorityEdit.setAuthority(authItem.getAuthority());
                boolean isUpdate = authorityService.editAuthority(authorityEdit);
                if (isUpdate) {
                    return new ResponseEntity<String>("0", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("1", HttpStatus.OK);

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("1", HttpStatus.OK);
        }
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<ApiResponseV1> deleteAuthority(@RequestBody Authority authItem, HttpServletRequest request) {
        String page = "0";  // 0: no error, 1: error, 2: not required, 3 not exits, 4 assigned, 5 had auth child
        try {
            if (authItem == null || authItem.getId() == 0L) {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,3,"không tồn tại", null), HttpStatus.OK);
            }
            Authority authorityDel = authorityService.getAuthorityById(authItem.getId());
            if (authorityDel == null) {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,3,"không tồn tại", null), HttpStatus.OK);
            }

            List<Authority> authorityChild = authorityService.getAuthorityChildrenById(authItem.getId());
            if (authorityChild != null && !authorityChild.isEmpty() && authorityChild.size() > 0) {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,5,"không thể xóa chức năng này do có quyền con", null), HttpStatus.OK);
            }

            boolean checkAssigned = authorityService.checkAuthorityAssigned(authItem.getId());
            if (checkAssigned) {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,4,"không thể xóa chức năng này do đã được gán cho người dùng", null), HttpStatus.OK);
            }

            boolean isUpdate = authorityService.deleteAuthority(authorityDel);
            if (isUpdate) {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(true,"thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,10,"thất bại", null), HttpStatus.OK);

            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<ApiResponseV1>(new ApiResponseV1(false,10,"thất bại", null), HttpStatus.OK);
        }
    }

    private boolean checkRequired(Authority authority) {
        boolean result = false;
        if (authority.getAuthority() == null || authority.getAuthority().isEmpty()) {
            result = false;
        } else if (authority.getDescription() == null || authority.getDescription().isEmpty()) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

}
