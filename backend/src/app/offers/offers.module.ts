
import { Module } from "@nestjs/common";
import { PrismaService } from "../../../prisma/prisma/prisma.service";
import { JwtModule } from "@nestjs/jwt";
import { OffersService } from "./offers/offers_service";
import { OffersController} from "./offers/offers.controller";


@Module({
  imports: [JwtModule.register({})],
  controllers: [OffersController],
  providers:[OffersService, PrismaService],
  exports: [OffersService]
})
export class OffersModule {}