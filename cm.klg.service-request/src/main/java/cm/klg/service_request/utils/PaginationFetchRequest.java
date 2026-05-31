package cm.klg.service_request.utils;

import lombok.Builder;

@Builder
public record PaginationFetchRequest(int limit, int pageIndex) {}
