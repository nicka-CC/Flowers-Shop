
import { Module } from "@nestjs/common";
import { PrismaService } from "../../../prisma/prisma/prisma.service";
import { JwtModule } from "@nestjs/jwt";
import { AGZSService } from "./agzs/agzs_service";
import { AGZSController, ApplicationController, MainPhotoGallaryController } from "./agzs/agzs.controller";


@Module({
  imports: [JwtModule.register({})],
  controllers: [AGZSController, ApplicationController,MainPhotoGallaryController],
  providers:[AGZSService, PrismaService],
  exports: [AGZSService]
})
export class AGZSModule {}