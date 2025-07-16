import { ApiBearerAuth, ApiConsumes, ApiOperation, ApiTags } from "@nestjs/swagger";
import {
  Body,
  Controller, Delete, Get,
  Param,
  Patch,
  Post,
  Put, Query,
  Req,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from "@nestjs/common";
import { JwtAuthGuard } from "../../../jwt-auth.guard";
import { Express } from "express";
import { FileInterceptor } from "@nestjs/platform-express";
import { diskStorage } from "multer";
import { extname } from "node:path";
import { OffersService } from "./offers_service";
import { OffersDto } from "../../../dto/offers.dto";

@ApiTags("Offers")
@ApiBearerAuth()
@Controller("offers")
export class OffersController {
  constructor(private gasBallonsService: OffersService) {
  }

  @UseGuards(JwtAuthGuard)
  @Post("register")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  @ApiConsumes("multipart/form-data")
  @UseInterceptors(
    FileInterceptor("image", {
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
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: OffersDto, // DTO для данных
  ) {
    if (!file) {
      throw new Error("Изображение обязательно");
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto: OffersDto = {
      ...dto,
      image: imagePath,
    };
    const user = (req as any).user;
    return this.gasBallonsService.post(user, updatedDto);
  };

  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: "Газовый баллон успешно обновлён" })
  @ApiConsumes("multipart/form-data")
  @UseInterceptors(
    FileInterceptor("image", {
      storage: diskStorage({
        destination: "./uploads",
        filename: (req, file, callback) => {
          const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
          const fileName = `${uniqueSuffix}${extname(file.originalname)}`;
          callback(null, fileName);
        },
      }),
    }),
  )
  @Put(":id")
  async update(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File,
    @Param("id") id: string,
    @Body() dto: OffersDto,
  ) {
    if (!file) {
      throw new Error("Изображение обязательно");
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto: OffersDto = {
      ...dto,
      image: imagePath,
    };
    const user = (req as any).user;
    return this.gasBallonsService.put(user, Number(id), updatedDto);
  }


  @Get()
  @ApiOperation({ summary: "Газовый баллоны успешно получены" })
  async filter(
    @Req() req: Request,
    @Query('page') page: string = '1',
    @Query('limit') limit: string = '10',
  ) {
    const pageNumber = parseInt(page, 10);
    const pageSize = parseInt(limit, 10);

    const result = await this.gasBallonsService.get( pageNumber, pageSize);

    return {
      total: result.total,
      page: pageNumber,
      limit: pageSize,
      data: result.data.map(agz => ({
        ...agz
      })),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Delete(':id')
  async delete(
      @Req() req: Request,
     @Param("id") id: string,) {
    const user = (req as any).user;
    return this.gasBallonsService.delete(user, Number(id));
  }


}
