package com.sparta.javajyojo.test;

import com.sparta.javajyojo.entity.User;

public interface CommonTest {
    String ANOTHER_PREFIX = "another-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    String TEST_USER_NAME = "username1";
    String TEST_USER_PASSWORD = "A1234qwer!";
    User TEST_USER = User.builder()
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();
    User TEST_ANOTHER_USER = User.builder()
            .username(ANOTHER_PREFIX + TEST_USER_NAME)
            .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
            .build();
}
