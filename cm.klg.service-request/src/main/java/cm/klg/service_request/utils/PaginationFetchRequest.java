package cm.klg.service_request.utils;

import cm.klg.service_request.domain.common.InvalidPaginationDataException;
import lombok.Builder;

@Builder
public record PaginationFetchRequest(int limit, int pageIndex) {
  public static PaginationFetchRequest of(int limit, int pageIndex) {
    if (limit < 10 || limit > 100 || pageIndex < 0) {
      throw new InvalidPaginationDataException();
    }
    return new PaginationFetchRequest(limit, pageIndex);
  }
}
