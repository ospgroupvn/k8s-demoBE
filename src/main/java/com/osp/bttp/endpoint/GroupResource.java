package com.osp.bttp.endpoint;


import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.Result;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.CreateGroupRequest;
import com.osp.bttp.dao.model.entity.db3.Authority;
import com.osp.bttp.dao.model.entity.db3.Group;
import com.osp.bttp.dao.model.mview.AuthorityView;
import com.osp.bttp.dao.model.mview.GroupView;
import com.osp.bttp.dao.service.GroupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 12/27/2017.
 */
@RestController
@RequestMapping("/v1/api/system/group")
@Secured({ConstantAuthor.SYSTEM.system})
public class GroupResource {

    private Logger logger = LogManager.getLogger(GroupResource.class);
    @Autowired
    private GroupService groupService;


    @GetMapping("/search")
    @Secured(ConstantAuthor.Group.view)
    public ResponseEntity<?> search( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                     @RequestParam(value = "numberPerPage", required = false, defaultValue = "15") int numberPerPage,
                                     @RequestParam(value = "filterName", required = false, defaultValue = "") String filterName,
                                     @RequestParam(value = "type", required = false, defaultValue = "") Integer type
    ) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        page.setNumberPerPage(numberPerPage);
        page = groupService.page(filterName, page, type).orElse(new PagingResult());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }



    public void loadAuthorityToModel(Model model, List<Authority> items) {
        List<AuthorityView> list = new ArrayList<>();
        List<Authority> childrens = new ArrayList<>();
        for (Authority item : items) {
            if (item.getFid() == 0) {
                AuthorityView au = new AuthorityView();
                au.setParent(item);
                list.add(au);
            }
        }

        for (AuthorityView item : list) {
            childrens = new ArrayList<>();
            for (Authority authority : items) {
                if (authority.getFid() == item.getParent().getId()) {
                    childrens.add(authority);
                }
            }
            item.setChildrens(childrens);
        }
        model.addAttribute("groups", list);
    }
    public List<AuthorityView> loadAuthorityToModelV1(List<Authority> items) {
        List<AuthorityView> list = new ArrayList<>();
        List<Authority> childrens = new ArrayList<>();
        for (Authority item : items) {
            if (item.getFid() == 0) {
                AuthorityView au = new AuthorityView();
                au.setParent(item);
                list.add(au);
            }
        }

        for (AuthorityView item : list) {
            childrens = new ArrayList<>();
            for (Authority authority : items) {
                if (authority.getFid() == item.getParent().getId()) {
                    childrens.add(authority);
                }
            }
            item.setChildrens(childrens);
        }
        return list;
    }


    @PostMapping("/add")
    @Secured(ConstantAuthor.Group.add)
    public ResponseEntity<ApiResponseV1<?>> groupAddSave(@RequestBody @Valid CreateGroupRequest item, HttpServletRequest request) {
        if (item.getGroupName() == null || item.getGroupName().isEmpty()) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, "message.groupname.not.isempty", null), HttpStatus.OK);
        }
        if (H.isTrue(groupService.checkBeforeAdd(item))) {
            return new ResponseEntity<>(groupService.checkBeforeAdd(item), HttpStatus.BAD_REQUEST);
        }
        if(H.isTrue(item.getIsDefault()) && item.getIsDefault().equals(1L) && !H.isTrue(item.getType()) ) {
            return new ResponseEntity<>(new ApiResponseV1<>(false,23, "Nhóm quyền mặc định phải có loại nhóm quyền trong trường hợp isDefault có giá trị 1!", null), HttpStatus.OK);
        }
        try {
            if (groupService.saveGroupView(item).orElse(false)) {
                return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Have error GroupController.groupAddSave: " + e.getMessage());
        }
        return new ResponseEntity<>(new ApiResponseV1<>(false, "message.group.message.add.fail", null), HttpStatus.OK);
    }


    @GetMapping("/detail/{id}")
    @Secured(ConstantAuthor.Group.view)
    public ApiResponseV1<?> detailGroup(@PathVariable Long id) {
        if (id == null || id.intValue() == 0) {
            return new ApiResponseV1<>(false, Result.INVALID_PARAM);
        }
        GroupView item = groupService.getGroupView(id).orElse(null);
        List<Authority> items = groupService.loadAllAuthority().orElse(new ArrayList<>());
        if(!H.isTrue(item) || !H.isTrue(items)) {
            return new ApiResponseV1<>(false, "Thất bại", null);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("item", item);
        map.put("listAuthority", loadAuthorityToModelV1(items));
        return new ApiResponseV1<>(true, 1, "Thành công", map);
    }


    @PostMapping("/edit")
    @Secured(ConstantAuthor.Group.edit)
    public ResponseEntity<ApiResponseV1<?>> groupEditSave(@RequestBody CreateGroupRequest item, HttpServletRequest request) {
        try {
            if (H.isTrue(groupService.checkBeforeEdit(item))) {
                return new ResponseEntity<>(groupService.checkBeforeEdit(item), HttpStatus.BAD_REQUEST);
            }
            if (item.getId() == null || item.getId().intValue() == 0) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 11, "Giá trị id truyền không đúng", null), HttpStatus.OK);
            }
            if (item.getGroupName() == null || item.getGroupName().isEmpty()) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 12, "Tên nhóm quyền không hợp lệ!", null), HttpStatus.OK);
            }
            if (H.isTrue(item.getIsDefault()) && item.getIsDefault().equals(1L) && !H.isTrue(item.getType())) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 23, "Nhóm quyền mặc định phải có loại nhóm quyền trong trường hợp isDefault có giá trị 1!", null), HttpStatus.OK);
            }
            try {
                if (groupService.editGroupView(item).orElse(false)) {
                    return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);
                }
            } catch (Exception e) {
                logger.error("Have error GroupController.groupEditSave: " + e.getMessage());
            }
            return new ResponseEntity<>(new ApiResponseV1<>(false, 10, "Thất bại", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 15, "Thất bại", null), HttpStatus.OK);
        }
    }


    @PostMapping("/delete")
    @Secured(ConstantAuthor.Group.delete)
    public ApiResponseV1<?> GroupDelete(Long id, HttpServletRequest request) {
        long check = 0L;
        String groupName = "";
        try {
            Group group = groupService.get(id).orElse(null);
            groupName = ( H.isTrue(group) && H.isTrue(group.getGroupName()) ) ? group.getGroupName() : "";
            check = groupService.deleteGroup(id).orElse(0L);
        } catch (Exception e) {
            logger.error("have an error UserDelete:" + e.getMessage());
        }
        switch (Integer.parseInt(check +"")) {
            case 0:
                return new ApiResponseV1<>(false, 10,"Lỗi không xác định", null);
            case 2:
                return new ApiResponseV1<>(false, 11,"Xóa nhóm thất bại. Nhóm quyền " + groupName + " đang được phân quyền cho người dùng!", null);
            case 1:
                return new ApiResponseV1<>(true, 1, "Thành công", null);
            default:
                return new ApiResponseV1<>(false, 13,"Lỗi không xác định", null);
        }

    }


    @GetMapping("/search-user-by-group-{groupId}")
    @Secured(ConstantAuthor.Group.user)
    public ApiResponseV1<?> ViewUserByGroup(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                            @PathVariable long groupId) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        page.setNumberPerPage(10);
        if (groupId == 0) {
            return new ApiResponseV1<>(false, "message.have.error", null);
        }
        page = groupService.pageUserOfGroup(groupId, page).orElse(new PagingResult());
        return new ApiResponseV1<>(true,1, "message.have.success", page);
    }


    @GetMapping("/search-authority-by-group-{groupId}")
    public ApiResponseV1<?> ViewAuthorityGroup(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                               @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") int numberPerPage,
                                               @PathVariable long groupId) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        page.setNumberPerPage(numberPerPage);
        if (groupId == 0) {
            return new ApiResponseV1<>(false, "message.have.error", null);
        }
        page = groupService.pageAuthorityOfGroup(groupId, page).orElse(new PagingResult());
        return new ApiResponseV1<>(true, 1,"message.have.success", page);
    }


    //add user to group
    @PostMapping("/add-user-to-group")
    @Secured(ConstantAuthor.Group.user)
    public ApiResponseV1<?> addUserToGroup(@RequestParam(value = "groupIds", required = true, defaultValue = "0") String groupIds,
                                           @RequestParam(value = "userId", required = true, defaultValue = "0") long userId,
                                           HttpServletRequest request) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new ApiResponseV1<>(false, 15,"Danh sách nhóm quyền không hợp lệ!", null);
        }
        if (userId == 0) {
            return new ApiResponseV1<>(false, 16,"Id người dùng không hợp lệ!", null);
        }
        try {
            if (groupService.addUserToGroup(groupIds, userId).orElse(false)) {
                return new ApiResponseV1<>(true, 1, "message.group.add.success", null);
            }
        } catch (Exception e) {
            logger.error("Have error GroupController.addUserToGroup: " + e.getMessage());
        }

        return new ApiResponseV1<>(false, 17,"Lỗi không xác định", null);
    }


}
