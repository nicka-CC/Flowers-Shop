import { TechnicalGasBalloonController } from "./gas_ballons/gas_ballons.controller";
import { Module } from "@nestjs/common";
import { GasBallonsService } from "./gas_ballons/gas_ballons_service";
import { PrismaService } from "../../../prisma/prisma/prisma.service";
import { JwtModule } from "@nestjs/jwt";
import { ServicesGasBalloonController } from "./gas_ballons_services/gas_ballons.controller";
import { ServiceGasBallonsService } from "./gas_ballons_services/gas_ballons_service";

@Module({
  imports: [JwtModule.register({})],
  controllers: [TechnicalGasBalloonController,ServicesGasBalloonController],
  providers:[GasBallonsService, PrismaService,ServiceGasBallonsService],
  exports: [GasBallonsService,ServiceGasBallonsService]
})
export class BalloonsModule {}