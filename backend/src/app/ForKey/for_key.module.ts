
import { Module } from "@nestjs/common";
import { PrismaService } from "../../../prisma/prisma/prisma.service";
import { JwtModule } from "@nestjs/jwt";
import { For_keyController} from "./ForKey/for_key.controller";
import { ForKeyService } from "./ForKey/for_key_service";


@Module({
  imports: [JwtModule.register({})],
  controllers: [For_keyController],
  providers:[ForKeyService, PrismaService],
  exports: [ForKeyService]
})
export class For_keyModule {}