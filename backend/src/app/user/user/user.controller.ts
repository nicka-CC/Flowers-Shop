import { ApiBearerAuth, ApiConsumes, ApiOperation, ApiTags } from "@nestjs/swagger";
import {
  Body,
  Controller, Delete, Get,
  Param, Patch,
  Post,
  Put, Query,
  Req,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from "@nestjs/common";
import { JwtAuthGuard } from "../../../jwt-auth.guard";
import { GetUserId } from "../../../user/auth/get-user-id.decorator";
import { UpdateUserDto } from "../../../dto/user.dro";
import { UserService } from "./user_service";
import { FileInterceptor } from "@nestjs/platform-express";
import { diskStorage } from "multer";
import { extname } from "node:path";
import { NewsDto } from "../../../dto/news.dto";
import { Express } from "express";
@ApiTags('User')
@ApiBearerAuth()
@UseGuards(JwtAuthGuard)
@Controller('user')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Get('me')
  getMe(@GetUserId() userId: number) {
    return this.userService.getUserInfo(userId);
  }

  @Patch('me')
  @ApiConsumes("multipart/form-data")
  @UseInterceptors(
    FileInterceptor("face", {
      storage: diskStorage({
        destination: "./uploads", // Папка для сохранения изображений
        filename: (req, file, callback) => {
          const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
          const fileName = `${uniqueSuffix}${extname(file.originalname)}`; // Генерируем уникальное имя файла
          callback(null, fileName);
        },
      }),
    }),
  )
  async register(
    @GetUserId() userId: number,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: UpdateUserDto // DTO для данных
  ) {
    if (!file) {
      throw new Error("Изображение обязательно");
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto:  UpdateUserDto = {
      ...dto,
      face: imagePath,
    };
    return this.userService.updateUser(userId, updatedDto);
  };


  @Delete('me')
  deleteMe(@GetUserId() userId: number) {
    return this.userService.deleteUser(userId);
  }
}