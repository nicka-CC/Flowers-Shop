import { Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from '../../../../prisma/prisma/prisma.service';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto"
import { AGZSDto, AGZSPhotoDto, ApplicationDto, MainPhotoGalaryDto } from "../../../dto/agzs.dto";
import { CommentsDto } from "../../../dto/comments.dto";
import { OffersDto } from "../../../dto/offers.dto";

@Injectable()
export class OffersService {
  constructor(private prisma:PrismaService) {}
  async post(user,dto:OffersDto){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.eventAndOffers.create({
      data: {
        image:dto.image,
        condition:dto.condition,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async put(user, gas_ballon_id,dto:OffersDto){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const updatedGasBallon = await this.prisma.eventAndOffers.update({where:{id:Number(gas_ballon_id)},
    data: {
      image:dto.image,
      condition:dto.condition,
      date_updated: new Date().toISOString(),
    }})
    return updatedGasBallon;
  }
  async get(page:number, limit:number) {
    const skip = (page - 1) * limit;
    const total = await this.prisma.eventAndOffers.count({});
    const data = await this.prisma.eventAndOffers.findMany({
      skip,
      take:limit,
    });
    return {total, data };
  }

  async delete(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.eventAndOffers.delete({where:{id:Number(id)}});
    return deleteRecord;
  }
}