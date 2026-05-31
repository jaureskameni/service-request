package cm.klg.service_request.domain.user;

import cm.klg.common.base.domain.PhoneNumber;
import org.jspecify.annotations.Nullable;

public record UserProfile(
    @Nullable Firstname firstname,
    Lastname lastname,
    @Nullable EmailAddress email,
    PhoneNumber phoneNumber) {}
