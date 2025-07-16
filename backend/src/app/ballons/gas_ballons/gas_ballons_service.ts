import { Injectable, InternalServerErrorException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from '../../../../prisma/prisma/prisma.service';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto"
import { BallonDto } from "../../../dto/ballons/ballon.dto";

@Injectable()
export class GasBallonsService {
  constructor(private prisma:PrismaService) {}
  async post(user,dto:BallonDto){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const newGasBallon = await this.prisma.ballons.create({
      data: {
        image:dto.image,
        name:dto.name,
        volume:dto.volume,
        price_filling: dto.price_filling,
        price_buy: dto.price_buy,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
      }
    });
    return newGasBallon;
  }
  async put(user, gas_ballon_id,dto:BallonDto){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const updatedGasBallon = await this.prisma.ballons.update({where:{id:Number(gas_ballon_id)},
    data: {
      image:dto.image,
      name:dto.name,
      volume:dto.volume,
      price_filling: dto.price_filling,
      price_buy: dto.price_buy,
      date_created: new Date().toISOString(),
    }})
    return updatedGasBallon;
  }
  async get(page:number, limit:number) {
    const skip = (page - 1) * limit;
    const total = await this.prisma.ballons.count({});
    const data = await this.prisma.ballons.findMany({
      skip,
      take:limit,
    });
    return {total, data };
  }

  async delete(user,id:number){
    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges users');
    }
    const deleteRecord = await this.prisma.ballons.delete({where:{id:Number(id)}});
    return deleteRecord;
  }


}