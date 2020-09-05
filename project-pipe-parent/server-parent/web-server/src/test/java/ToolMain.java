import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoginBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthDMACode;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.water.tool.service.BaseInf;
import com.dotop.water.tool.util.DataUtil;

public class ToolMain {

    public static void main(String[] args) {
//        String uuid = UuidUtils.getUuid();
//        System.out.println(uuid);
        method();
    }

    public static void method() {
       WaterClientConfig.WaterCasUrl = "http://localhost:39999/water-cas";

        // 以下为例子
        try {
            //todo 在此修改账号、密码和域名
            String PlatformUser = BaseInf.webLogin("zhangjiakou", "123456", "localhost:4600");
//            String PlatformUser = BaseInf.webLogin("zhangjiakou", "123456", "localhost:4700");

            JSONObject parseObject = JSON.parseObject(PlatformUser);
            String userid = parseObject.getString("userid");
            String ticket = parseObject.getString("ticket");

            UserLoginBo userLogin = new UserLoginBo();
            userLogin.setUserid(userid);
            userLogin.setTicket(ticket);
//             userLogin.setModelid(DmaCode.Platform_Dma);
            //todo 在此修改模块编号
            userLogin.setModelid(AuthDMACode.Company_Dma);
//            userLogin.setModelid("60000000");
            String generateCasKey = DataUtil.generateCasKey(userLogin);
            System.out.println("----");
            System.out.println(generateCasKey);
            System.out.println("----");

            String cas = generateCasKey;
            UserLoginVo userLogin2 = DataUtil.getUserLogin(cas);
            System.out.println(JSON.toJSONString(userLogin2));


        } catch (BusinessException ex) {
            System.out.println(ex.getErrorCode());
        }
    }
}
