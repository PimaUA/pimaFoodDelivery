package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.dto.restaurant.OpeningHoursUpdateDto;
import com.pimaua.core.entity.restaurant.OpeningHours;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OpeningHoursMapper {
    OpeningHours toEntity(OpeningHoursRequestDto openingHoursRequestDto);

    OpeningHoursResponseDto toDto(OpeningHours openingHours);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    void updateEntity(@MappingTarget OpeningHours openingHours, OpeningHoursUpdateDto openingHoursUpdateDto);

    List<OpeningHoursResponseDto> toListDto(List<OpeningHours> openingHoursList);
}
