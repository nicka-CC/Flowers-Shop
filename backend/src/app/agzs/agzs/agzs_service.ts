import { Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from '../../../../prisma/prisma/prisma.service';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto"
import {
  AGZSDto,
  AGZSPhotoDto,
  ApplicationDto, ApplicationOnCalculateDto,
  ApplicationOnDeliveryDto,
  ApplicationOnKeepDto,
  MainPhotoGalaryDto,
} from "../../../dto/agzs.dto";

@Injectable()
export class AGZSService {
  constructor(private prisma:PrismaService) {}
  async post(user,dto:AGZSDto){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.agzs.create({
      data: {
        name:dto.name,
        status:dto.status,
        street:dto.street,
        price:Number(dto.price),
        price_in_offer:Number(dto.price_in_offer),
        position1:dto.position1,
        position2:dto.position2,
        position3:dto.position3,
        position4:dto.position4,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async put(user, gas_ballon_id,dto:AGZSDto){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const updatedGasBallon = await this.prisma.agzs.update({where:{id:Number(gas_ballon_id)},
    data: {
      name:dto.name,
      status:dto.status,
      street:dto.street,
      price:Number(dto.price),
      price_in_offer:Number(dto.price_in_offer),
      position1:dto.position1,
      position2:dto.position2,
      position3:dto.position3,
      position4:dto.position4,
      date_updated: new Date().toISOString(),
    }})
    return updatedGasBallon;
  }
  async get(status:string,page:number, limit:number) {
    const skip = (page - 1) * limit;
    const filterCondition = status ? {
      status:{
        contains: `${status}`,
      },
    }:{};
    const total = await this.prisma.agzs.count({where:filterCondition});
    const data = await this.prisma.agzs.findMany({
      where:filterCondition,
      skip,
      take:limit,
    });
    return {total, data };
  }

  async delete(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.agzs.delete({where:{id:Number(id)}});
    return deleteRecord;
  }
  async postPhoto(user,dto:AGZSPhotoDto, id){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.agzs_photogallary.create({
      data: {
        image:dto.photo,
        agzsId:Number(id),
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async getPhoto(agzsId?:number, photoId?:number,page?:number, limit?:number) {
    const skip = page && limit ? (page - 1) * limit : 0;
    const where: any = {};
    if (agzsId !== undefined) where.agzsId = Number(agzsId);
    if (photoId !== undefined) where.id = Number(photoId);

    const total = await this.prisma.agzs_photogallary.count({
      where,
    });
    const data = await this.prisma.agzs_photogallary.findMany({
      where:where,
      skip,
      take:limit,
    });
    return {total, data };
  }
  async deletePhoto(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.agzs_photogallary.delete({where:{id:Number(id)}});
    return deleteRecord;
  }
  async postApplication(dto:ApplicationDto){
    const newGasBallon = await this.prisma.applications.create({
      data: {
        name:dto.name,
        status:dto.status,
        phone:dto.phone,
        about_status:dto.about_status,
        about_where:dto.about_where,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async postApplicationonKeep(dto:ApplicationOnKeepDto){
    const newGasBallon = await this.prisma.applicationOnKeeping.create({
      data: {
        volume:dto.volume,
        status:dto.status,
        phone:dto.phone,
        description_status:dto.description_status,
        period:dto.period,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async postApplicationonDelivery(dto:ApplicationOnDeliveryDto){
    const newGasBallon = await this.prisma.optionForApplication.create({
      data: {
        name:dto.name,
        status:dto.status,
        phone:dto.phone,
        description_status:dto.description_status,
        address:dto.address,
        option:dto.option,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async postApplicationonOption(dto:ApplicationOnDeliveryDto){
    const newGasBallon = await this.prisma.optionForService.create({
      data: {
        name:dto.name,
        status:dto.status,
        phone:dto.phone,
        description_status:dto.description_status,
        address:dto.address,
        option:dto.option,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async postApplicationonCalculate(dto:ApplicationOnCalculateDto){
    const newGasBallon = await this.prisma.application_on_calculate.create({
      data: {
        name:dto.name,
        status:dto.status,
        phone:dto.phone,
        description_status:dto.description_status,
        address:dto.address,
        variant:dto.variant,
        date:dto.date,
        total:Number(dto.total),
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }

  async postPhotoGallary(user,dto:MainPhotoGalaryDto, id){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.photogallary_main.create({
      data: {
        image:dto.image,
        module:dto.module,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async getPhotoGallary( module:string,page:number, limit:number) {
    const skip = page && limit ? (page - 1) * limit : 0;
    const where: any = {};

    if (module !== undefined) where.module = module;

    const total = await this.prisma.photogallary_main.count({
      where,
    });
    const data = await this.prisma.photogallary_main.findMany({
      where:where,
      skip,
      take:limit,
    });
    return {total, data };
  }
  async deletePhotoGallary(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.photogallary_main.delete({where:{id:Number(id)}});
    return deleteRecord;
  }
}