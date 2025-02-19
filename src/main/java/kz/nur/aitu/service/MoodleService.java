package kz.nur.aitu.service;

import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.exception.MoodleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MoodleService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String MOODLE_BASE_URL = "https://moodle.astanait.edu.kz/webservice/rest/server.php";

    public UserDto getUserInfoFromMoodle(String securityKey) {
        String siteInfoUrl = MOODLE_BASE_URL +
                "?wstoken=" + securityKey +
                "&wsfunction=core_webservice_get_site_info&moodlewsrestformat=json";

        Map<String, Object> siteInfoResponse = restTemplate.getForObject(siteInfoUrl, Map.class);
        if (siteInfoResponse == null || !siteInfoResponse.containsKey("username")) {
            throw new MoodleException("Не удалось получить данные из Moodle.");
        }

        String username = (String) siteInfoResponse.get("username");
        String firstName = (String) siteInfoResponse.get("firstname");
        String lastName = (String) siteInfoResponse.get("lastname");

        String userInfoUrl = MOODLE_BASE_URL +
                "?wstoken=" + securityKey +
                "&wsfunction=core_user_get_users_by_field&moodlewsrestformat=json" +
                "&field=username&values[0]=" + username;

        Map[] userInfoResponse = restTemplate.getForObject(userInfoUrl, Map[].class);
        if (userInfoResponse == null || userInfoResponse.length == 0) {
            throw new MoodleException("Не удалось получить полные данные пользователя из Moodle.");
        }

        Map<String, Object> userInfo = userInfoResponse[0];
        Long id = Long.valueOf(userInfo.get("id").toString());
        String department = (String) userInfo.get("department");

        return UserDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(username)
                .department(department)
                .build();
    }
}
