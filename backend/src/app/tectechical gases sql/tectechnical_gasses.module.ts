import { TechnicalGasBalloonController } from "./gas_ballons/gas_ballons.controller";
import { Module } from "@nestjs/common";
import { GasBallonsSQLService } from "./gas_ballons/gas_ballons_service";
import { JwtModule } from "@nestjs/jwt";
import { DatabaseModule } from "../../database.module";


@Module({
  imports: [JwtModule.register({}), DatabaseModule],
  controllers: [TechnicalGasBalloonController],
  providers:[GasBallonsSQLService],
  exports: [GasBallonsSQLService]
})
export class TechnicalGasBalloonSQLModule {}