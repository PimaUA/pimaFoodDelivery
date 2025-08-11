package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.OpeningHours;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpeningHoursMapper {
    OpeningHours toEntity(OpeningHoursRequestDto openingHoursRequestDto);

    OpeningHoursResponseDto toDto(OpeningHours openingHours);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget OpeningHours openingHours, OpeningHoursRequestDto openingHoursRequestDto);

    List<OpeningHoursResponseDto> toListDto(List<OpeningHours> openingHoursList);
}
