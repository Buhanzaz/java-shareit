package shareit.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItTests {

	@Autowired
	ShareItApp shareItApp;

	@Test
	void contextLoads() {
	}

	@Test
	void shareItAppTest() {
		ShareItApp.main(new String[] {});
	}
}