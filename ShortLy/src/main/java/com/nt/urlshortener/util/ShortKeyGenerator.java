package com.nt.urlshortener.util;


import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;


@Component
public class ShortKeyGenerator {


public String generate(int length) {
// alphanumeric (base62-ish)
return RandomStringUtils.randomAlphanumeric(length);
}
}