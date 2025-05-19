package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.apigateway.dto.userservice.CreateRoleRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import com.github.fckng0d.grpc.userservice.CreateRoleRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    // CreateRoleRequestDto -> CreateRoleRequest
    CreateRoleRequest toCreateRoleRequest(CreateRoleRequestDto requestDto);
}