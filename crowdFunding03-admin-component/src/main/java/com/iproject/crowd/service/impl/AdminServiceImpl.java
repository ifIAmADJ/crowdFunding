package com.iproject.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iproject.crowd.constant.ProjectConstant;
import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.entity.AdminExample;
import com.iproject.crowd.exception.AcctNotUniqueException;
import com.iproject.crowd.exception.AcctNotUniqueForUpdateException;
import com.iproject.crowd.exception.LoginFailureException;
import com.iproject.crowd.mapper.AdminMapper;
import com.iproject.crowd.service.api.AdminService;
import com.iproject.crowd.utils.Md5Helper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.iproject.crowd.constant.ProjectConstant.MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void saveAdmin(Admin admin) {

        // 使用 MD5 的加密串替换密码
        String safePwd = Md5Helper.md5(admin.getPwd());
        admin.setPwd(safePwd);

        Date createTime = new Date();
        admin.setCreateTime(createTime);

        try {

            adminMapper.insert(admin);

        } catch (Exception e) {

            LoggerFactory.getLogger(this.getClass()).warn(e.getMessage());
            if (e instanceof DuplicateKeyException) throw
                    new AcctNotUniqueException(MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE);
        }
    }

    @Override
    public List<Admin> getAll() {
//      该方法传入 null 则相当于进行无条件查询
        return adminMapper.selectByExample(null);
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String loginPlainPwd) {

        // 1. 根据 loginAcct 尝试查询出 Admin 实体
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();

        criteria.andAccountEqualTo(loginAcct);

        // 2. 判断 Admin 是否为空，传入的是 adminExample!
        List<Admin> admins = adminMapper.selectByExample(adminExample);

        // 3. 为 null，则抛出 LoginFailureException 异常。
        if (admins == null || admins.size() == 0) {
            throw new LoginFailureException(ProjectConstant.MESSAGE_LOGIN_INVALID);
        }

        // 3.1 账号应该是唯一的，这一种情况同样不能发生。
        if (admins.size() > 1) throw new RuntimeException(ProjectConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);

        // 4. 不是 null，则取出 Admin 的 pwd 属性。
        Admin admin = admins.get(0);

        // 5. 将用户传入的明文密码经过 md5 加密，并进行比对。
        String pwd_db = admin.getPwd();
        String pwd_form = Md5Helper.md5(loginPlainPwd);

        // 6. 若不一致，则抛出异常。若一致，则将该对象返回。
        // Objects.equals(a,b）还可以避免空指针异常。
        if (!Objects.equals(pwd_db, pwd_form)) throw new LoginFailureException(ProjectConstant.MESSAGE_LOGIN_INVALID);

        return admin;
    }

    /**
     * @param keyword  搜索的关键字。
     * @param pageNum  搜索的页号
     * @param pageSize 一页显示多少内容？
     * @return 返回指定页号的内容。
     */
    @Override
    public PageInfo<Admin> getAdminPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1. 调用 PageHelper 的静态方法开启分页功能。
        // 这里充分体现了 PageHelper 的非侵入式设计，原本的业务不需要做任何修改。
        PageHelper.startPage(pageNum, pageSize);

        // 2. 执行查询
        List<Admin> admins = adminMapper.selectAdminByKey(keyword);

        // 3. 封装到 PageInfo 对象中。
        return new PageInfo<>(admins);

    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Admin admin) {

        try {
            //表示有选择的更新
            adminMapper.updateByPrimaryKeySelective(admin);

        } catch (Exception e) {

            LoggerFactory.getLogger(this.getClass()).warn(e.getMessage());
            if (e instanceof DuplicateKeyException) throw
                    new AcctNotUniqueForUpdateException(MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE);
        }

    }

}
