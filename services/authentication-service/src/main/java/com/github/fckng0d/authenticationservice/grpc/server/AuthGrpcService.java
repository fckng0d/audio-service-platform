//package com.github.fckng0d.authenticationservice.grpc.server;
//
//import com.github.fckng0d.grpc.AuthServiceGrpc;
//import com.github.fckng0d.grpc.TokenRequest;
//import com.github.fckng0d.grpc.ValidationResponse;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;
//
//import java.util.List;
//
//@GrpcService
//public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {
////    private final JwtValidator jwtValidator;
////
////    @Override
////    public void validateToken(
////            TokenRequest request,
////            StreamObserver<ValidationResponse> responseObserver
////    ) {
////        boolean isValid = jwtValidator.validateToken(request.getToken());
////        String username = isValid ? jwtValidator.getUsernameFromToken(request.getToken()) : "";
////        List<String> roles = isValid ? jwtValidator.getRolesFromToken(request.getToken()) : List.of();
////
////        responseObserver.onNext(ValidationResponse.newBuilder()
////                .setValid(isValid)
////                .setUsername(username)
////                .addAllRoles(roles)
////                .build());
////        responseObserver.onCompleted();
////    }
////
////    @Override
////    public void getUserInfo(
////            TokenRequest request,
////            StreamObserver<UserInfoResponse> responseObserver
////    ) {
////        if (jwtValidator.validateToken(request.getToken())) {
////            String username = jwtValidator.getUsernameFromToken(request.getToken());
////            List<String> roles = jwtValidator.getRolesFromToken(request.getToken());
////
////            responseObserver.onNext(UserInfoResponse.newBuilder()
////                    .setUsername(username)
////                    .addAllRoles(roles)
////                    .build());
////        } else {
////            responseObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());
////        }
////        responseObserver.onCompleted();
////    }
//}
