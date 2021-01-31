import com.heima.admin.AdminApplication;
import com.heima.admin.service.WmNewsCensorshipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cuichacha
 * @date 1/31/21 20:36
 */
@SpringBootTest(classes = AdminApplication.class)
@RunWith(SpringRunner.class)
public class WeMediaCensorshipTest {

    @Autowired
    private WmNewsCensorshipService wmNewsCensorshipService;

    @Test
    public void test1() {
        wmNewsCensorshipService.censorByWmNewsId(6237);
    }
}
