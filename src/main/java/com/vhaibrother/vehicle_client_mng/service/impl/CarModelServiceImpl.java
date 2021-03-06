package com.vhaibrother.vehicle_client_mng.service.impl;

import com.vhaibrother.vehicle_client_mng.dto.CarModelDto;
import com.vhaibrother.vehicle_client_mng.dto.Response;
import com.vhaibrother.vehicle_client_mng.entity.CarModel;
import com.vhaibrother.vehicle_client_mng.enums.ActiveStatus;
import com.vhaibrother.vehicle_client_mng.repository.CarModelRepository;
import com.vhaibrother.vehicle_client_mng.service.CarModelService;
import com.vhaibrother.vehicle_client_mng.util.ResponseBuilder;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarModelServiceImpl implements CarModelService {
    private final CarModelRepository carModelRepository;
    private final ModelMapper modelMapper;
    private final String root = "CarModel";

    public CarModelServiceImpl(CarModelRepository carModelRepository, ModelMapper modelMapper) {
        this.carModelRepository = carModelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response save(CarModelDto carModelDto) {
        CarModel carModelName = getModelByName(carModelDto);
        if (carModelName != null) {
            return ResponseBuilder.getFailureResponse(HttpStatus.IM_USED, "This" + root + "Already Created");
        }
        CarModel carModel;
        carModel = modelMapper.map(carModelDto, CarModel.class);
        carModel = carModelRepository.save(carModel);
        if (carModel != null) {
            return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, root + "Has been Created", null);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @Override
    public Response update(Long id, CarModelDto carModelDto) {
        CarModel carModelName = getModelByName(carModelDto);
        if (carModelName != null && carModelName.getId() != id) {
            return ResponseBuilder.getFailureResponse(HttpStatus.IM_USED, "This " + root + "Already Created");
        }
        CarModel carModel = carModelRepository.getByIdAndActiveStatusTrue(id, ActiveStatus.ACTIVE.getValue());
        if (carModel != null) {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            carModel = modelMapper.map(carModelDto, CarModel.class);
            carModel = carModelRepository.save(carModel);
            if (carModel != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, root + " updated Successfully", null);
            }

            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error Occurs");
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
    }

    @Override
    public Response getById(Long id) {
        CarModel carModel = carModelRepository.getByIdAndActiveStatusTrue(id, ActiveStatus.ACTIVE.getValue());
        if (carModel != null) {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            CarModelDto carModelDto = modelMapper.map(carModel, CarModelDto.class);
            return ResponseBuilder.getSuccessResponse(HttpStatus.OK, root + " retrieved Successfully", carModelDto);
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
    }

    @Override
    public Response del(Long id) {
        CarModel carModel = carModelRepository.getByIdAndActiveStatusTrue(id, ActiveStatus.ACTIVE.getValue());
        if (carModel != null) {
            carModel.setActiveStatus(ActiveStatus.DELETE.getValue());
            carModel = carModelRepository.save(carModel);
            if (carModel != null) {
                return ResponseBuilder.getSuccessResponse(HttpStatus.OK, root + "Delete SucessFully", null);
            }
            return ResponseBuilder.getFailureResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
    }

    @Override
    public Response getAll() {
        List<CarModel> carModelList = carModelRepository.list(ActiveStatus.ACTIVE.getValue());
        List<CarModelDto> carModelDto = this.getCarModel(carModelList);
        if (carModelDto.isEmpty() || carModelDto == null) {
            return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, "There is No" + root);
        }
        return ResponseBuilder.getSuccessResponse(HttpStatus.OK, root + "Data Retrieve Successfully", carModelDto);
    }

    private CarModel getModelByName(CarModelDto carModelDto) {
        CarModel carModel = carModelRepository.getCarModelsByCarModelName(carModelDto.getCarModelName());
        return carModel;
    }

    private List<CarModelDto> getCarModel(List<CarModel> carModel) {
        List<CarModelDto> carModelDtoList = new ArrayList<>();
        carModel.forEach(model -> {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            CarModelDto carModelDto = modelMapper.map(model, CarModelDto.class);
            carModelDtoList.add(carModelDto);
        });
        return carModelDtoList;
    }
}
