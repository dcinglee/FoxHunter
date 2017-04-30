package com.foxhunter.pay.web;

import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.util.Converter;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import com.foxhunter.custom.entity.Custom;
import com.foxhunter.custom.service.CustomService;
import com.foxhunter.pay.entity.Bill;
import com.foxhunter.pay.service.BillService;
import com.foxhunter.photo.entity.Photo;
import com.foxhunter.photo.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/pay")
public class PayController {
    private Logger logger = LoggerFactory.getLogger(PayController.class);

    private BillService billService;

    private CustomService customService;

    private PhotoService photoService;//照片

    @Autowired
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Autowired
    public void setBillService(BillService billService) {
        this.billService = billService;
    }

    @Autowired
    public void setCustomService(CustomService customService) {
        this.customService = customService;
    }

    /**
     * 客户提现。
     */
    @ResponseBody
    @RequestMapping("/withdraw")
    @AuthLogin(needCustom = true)
    public ResultMessage withdrawCash(Custom custom) {
        try {
            if (custom == null || custom.getOpenId() == null) {
                return ResultMessage.newFailure("没有客户信息，请重新授权登陆！");
            }
            return billService.withdrawCash(custom);
        } catch (Exception e) {
            logger.error("客户提现异常：" + e.getMessage());
            return ResultMessage.newFailure("提现出错，请稍候再试！");
        }
    }

    /**
     * 查询客户的收益。
     */
    @ResponseBody
    @RequestMapping("/queryIncome")
    @AuthLogin(needCustom = true)
    public ResultMessage queryIncome(Custom custom) {
        Custom customQuery = customService.findCustomByOpenId(custom.getOpenId());
        if (null != customQuery && !"".equals(customQuery.getOpenId())) {
            return ResultMessage.newSuccess("查询收益成功！").setData(customQuery);
        } else {
            return ResultMessage.newFailure("查询收益失败！");
        }
    }

    /**
     * 客户查询客户账单的数据。
     */
    @ResponseBody
    @RequestMapping("/queryBill")
    @AuthLogin(needCustom = true)
    public ResultMessage queryBill(Custom custom, int page, int rows) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page<Bill> billPage = billService.findByCustomOpenId(custom.getOpenId(), pageable);
            if (null != billPage && billPage.getContent().size() > 0) {
                return ResultMessage.newSuccess().setData(billPage);
            } else {
                return ResultMessage.newFailure();
            }
        } catch (Exception e) {
            logger.error("查询客户信息异常：" + e.getMessage());
            return ResultMessage.newFailure("查询客户信息异常！");
        }
    }

    /**
     * 管理员查询提现信息。
     */
    @ResponseBody
    @RequestMapping("/list")
    @AuthLogin(needManager = true)
    public EasyUIGridData managerListBills(int page, int rows, String startTime, String endTime, String name, String billNo) {
        try {
            if (!StringUtils.hasText(startTime)) {
                return new EasyUIGridData("custom", "起始时间不能为空！");
            }
            Date startDateTime = Converter.StringToDateTime(startTime);
            Date endDateTime = Converter.StringToDateTime(endTime);
            // 避免查询不到最后一秒的账单
            endDateTime.setTime(endDateTime.getTime() + 999);
            Pageable pageable = new PageRequest(page - 1, rows);
            Page<Bill> dataPage = billService.findBills(startDateTime, endDateTime, name, billNo, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            logger.error("查询账单信息异常：" + e.getMessage());
            return new EasyUIGridData("custom", "查询账单信息异常！");
        }
    }

    /**
     * 客户查询照片的数据信息。
     */
    @ResponseBody
    @RequestMapping("/queryPhoto")
    @AuthLogin(needCustom = true)
    public ResultMessage listPhotosOfCustom(Custom custom, Integer checkState, int page, int rows) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page<Photo> photoPage = photoService.listPhotosOfCustom(custom.getOpenId(), checkState, pageable);
            if (null != photoPage && photoPage.getContent().size() > 0) {
                return ResultMessage.newSuccess().setData(photoPage);
            } else {
                return ResultMessage.newFailure("查询照片记录失败！");
            }
        } catch (Exception e) {
            logger.error("查询照片记录异常：" + e.getMessage());
            return ResultMessage.newFailure("查询照片记录异常！");
        }
    }

}
