import { Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from '../../../../prisma/prisma/prisma.service';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto"
import { AGZSDto, AGZSPhotoDto, ApplicationDto, MainPhotoGalaryDto } from "../../../dto/agzs.dto";
import { CommentsDto } from "../../../dto/comments.dto";
import { NewsDto } from "../../../dto/news.dto";
import { ForKeyDto } from "../../../dto/for_key.dto";

@Injectable()
export class ForKeyService {
  constructor(private prisma:PrismaService) {}
  async post(user,dto:ForKeyDto){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.for_key.create({
      data: {
        image:dto.image,
        gasGolder:dto.gasGolder,
        liter:Number(dto.liter),
        price:Number(dto.price),
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async put(user, gas_ballon_id,dto:ForKeyDto){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const updatedGasBallon = await this.prisma.for_key.update({where:{id:Number(gas_ballon_id)},
    data: {
      image:dto.image,
      gasGolder:dto.gasGolder,
      liter:Number(dto.liter),
      price:Number(dto.price),
      date_updated: new Date().toISOString(),
    }})
    return updatedGasBallon;
  }
  async get(page:number, limit:number) {
    const skip = (page - 1) * limit;
    const total = await this.prisma.for_key.count({});
    const data = await this.prisma.for_key.findMany({
      skip,
      take:limit,
    });
    return {total, data };
  }

  async delete(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.for_key.delete({where:{id:Number(id)}});
    return deleteRecord;
  }
}