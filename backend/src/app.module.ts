import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { PrismaService } from "../prisma/prisma/prisma.service";
import {AuthModule} from './user/auth/auth.module'
import { AppService } from "./app.service";
import { TechnicalGasBalloonModule } from "./app/tectechical gases/tectechnical_gasses.module";
import { TechnicalGasBalloonSQLModule } from "./app/tectechical gases sql/tectechnical_gasses.module";
import { DatabaseModule } from "./database.module";
import { BalloonsModule } from "./app/ballons/ballons.module";
import { AGZSModule } from "./app/agzs/agzs.module";
import { CommentModule } from "./app/comments/comment.module";
import { OffersModule } from "./app/offers/offers.module";
import { ForKeepingModule } from "./app/ForKeeping/forKeeping.module";
import { NewsModule } from "./app/news/news.module";
import { HistoryModule } from "./app/history/history.module";
import { For_keyModule } from "./app/ForKey/for_key.module";
import { ProcessModule } from "./app/process/process.module";
import { UserModule } from "./app/user/user.module";

@Module({
  imports: [
    AuthModule,
    TechnicalGasBalloonModule,
    TechnicalGasBalloonSQLModule,
    BalloonsModule,
    DatabaseModule,
    AGZSModule,
    CommentModule,
    OffersModule,
    ForKeepingModule,
    NewsModule,
    HistoryModule,
    For_keyModule,
    ProcessModule,
    UserModule
  ],
  controllers: [AppController],
  providers: [PrismaService, AppService],
  // exports: ['PG_POOL', GasBallonsSQLService],
})
export class AppModule {}
