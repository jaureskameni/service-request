package cm.klg.service_request.domain.service_provider;

import lombok.Value;
import org.jspecify.annotations.Nullable;

@Value
public class ProviderLocation {
  UserCityId cityId;
  UserDistrictId districtId;
  @Nullable UserQuarterId quarterId;
}
