package com.foodlab.foodReservation.testUtils;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * test class 가 실행될 때마다 새로 test container 를 생성하는 대신, 미리 만들어둔 test container 를 사용할 수 있게 하는 Abstract class
 * <a href="https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/">참고링크</a>
 */
@Testcontainers
@ActiveProfiles("test")
public class TestContainerBase {

    // TODO: 추후 docker-compose 파일을 통해 로드되는 Testcontainer 지정하도록 변경 (연결되는 서비스가 늘어나면)
    static final MariaDBContainer<?> mariadb;

    static {
        mariadb = new MariaDBContainer<>("mariadb:10.9.2");
        mariadb.start();
    }

    /**
     * 만약 테스트 별로 다른 ApplicationContext 를 사용해야 한다면 (이 메서드에서 ApplicationContext 를 변경하고 있다.)
     * 이 메서드를 여기서 없애고 각 테스트 클래스마다 각자 놓는 것이 더 나을수도 있다.
     */
    @DynamicPropertySource
    static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadb::getJdbcUrl);
        registry.add("spring.datasource.username", mariadb::getUsername);
        registry.add("spring.datasource.password", mariadb::getPassword);
    }
}
