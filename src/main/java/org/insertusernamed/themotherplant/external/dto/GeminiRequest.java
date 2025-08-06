package org.insertusernamed.themotherplant.external.dto;

import java.util.List;

public record GeminiRequest(List<Content> contents) {}

record InlineData(String mime_type, String data) {}
