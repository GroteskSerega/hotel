package com.example.hotel.aop;

import com.example.hotel.entity.RoleType;
import com.example.hotel.exception.ForbiddenException;
import com.example.hotel.exception.UserNotAuthenticatedException;
import com.example.hotel.security.AppUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static com.example.hotel.aop.AspectMessagesTemplates.*;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthoriseUsernameAspect {

    private static final String ANONYMOUS_USER = "anonymousUser";

    @Before("@annotation(AuthoriseUserCreateByAnonymous)")
    public void validateRoleTypeForAnonymousUserCreate(JoinPoint joinPoint) {
        HttpServletRequest request = getRequest();

        loggingOperation(joinPoint, request);

        Authentication auth = getAuth();

        if (!auth.getName().equals(ANONYMOUS_USER)) {
            AppUserPrincipal principal =
                    ((AppUserPrincipal) auth.getPrincipal());

            if (isAdmin(principal)) {
                return;
            }

            throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
        }

        if (isRoleTypeUser(joinPoint)) {
            return;
        }

        throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
    }

    @Before("@annotation(AuthoriseUserUpdateAndDelete)")
    public void validateAuthForUpdateDelete(JoinPoint joinPoint) {
        HttpServletRequest request = getRequest();

        loggingOperation(joinPoint, request);

        AppUserPrincipal principal = getUserDetails();

        if (isAdmin(principal)) {
            return;
        }

        if (!isRoleTypeUser(joinPoint)) {
            throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
        }

        UUID userId = getId(request);

        UUID userIdPrincipal = principal.getUserId();

        if (!userIdPrincipal.equals(userId)) {
            throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
        }
    }

    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes =
                RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    private void loggingOperation(JoinPoint joinPoint,
                                  HttpServletRequest request) {
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        log.info(CALL_OPERATION,
                auth.getName(),
                joinPoint.getSignature().getName(),
                pathVariables.toString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private AppUserPrincipal getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UserNotAuthenticatedException(TEMPLATE_OPERATION_UNAUTHORIZED);
        }

        return (AppUserPrincipal) auth.getPrincipal();
    }

    private Boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(ga ->
                        ga.getAuthority().equals(RoleType.ROLE_ADMIN.toString()));
    }

    private Boolean isRoleTypeUser(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof RoleType) {
                return ((RoleType) arg).name()
                        .equals(RoleType.ROLE_USER.toString());
            }
        }

        return false;
    }

    private UUID getId(HttpServletRequest request) {
        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables.isEmpty()) {
            throw new ForbiddenException(TEMPLATE_OPERATION_FORBIDDEN);
        }

        return UUID.fromString(pathVariables.get("id"));
    }
}
