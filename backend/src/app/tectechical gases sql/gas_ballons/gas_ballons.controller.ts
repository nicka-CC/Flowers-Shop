import { ApiBearerAuth, ApiConsumes, ApiOperation, ApiTags } from "@nestjs/swagger";
import {
  Body,
  Controller,
  Get,
  Param,
  Post,
  Put,
  Query,
  Req,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from "@nestjs/common";
import { GasBallonsSQLService } from "./gas_ballons_service";
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { JwtAuthGuard } from "../../../jwt-auth.guard";
import { FileInterceptor } from "@nestjs/platform-express";
import { diskStorage } from 'multer';
import * as path from 'node:path';
import { Multer } from 'multer';
import { extname } from 'node:path';
import { Express } from 'express';

@ApiTags('Technical_gas_balloon_SQL')
@ApiBearerAuth()
@Controller('techical_gas_balloon_sql')
export class TechnicalGasBalloonController {
  constructor(private gasBallonsSQLService: GasBallonsSQLService) {}

  @UseGuards(JwtAuthGuard)
  @Post('register')
  @ApiOperation({ summary: 'Газовый баллон успешно создан' })
  @ApiConsumes('multipart/form-data')
  @UseInterceptors(
    FileInterceptor('image', {
      storage: diskStorage({
        destination: './uploads', // Папка для сохранения изображений
        filename: (req, file, callback) => {
          const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
          const fileName = `${uniqueSuffix}${extname(file.originalname)}`; // Генерируем уникальное имя файла
          callback(null, fileName);
        },
      }),
    }),
  )
  async register(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: Gass_ballonDto, // DTO для данных
  ) {
    if (!file) {
      throw new Error('Изображение обязательно');
    }

    // Путь к изображению (можно изменить, если нужно хранить другой формат пути)
    const imagePath = `/uploads/${file.filename}`;

    // Добавляем путь к изображению в DTO
    const updatedDto: Gass_ballonDto = {
      ...dto,
      image: imagePath, // Записываем путь к файлу в базу данных
    };

    // Сохраняем данные в базе данных
    const user = (req as any).user; // Получаем пользователя из запроса
    return this.gasBallonsSQLService.post(user, updatedDto);
  }

  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: 'Газовый баллон успешно обновлён' })
  @ApiConsumes('multipart/form-data')
  @UseInterceptors(
    FileInterceptor('image', {
      storage: diskStorage({
        destination: './uploads',
        filename: (req, file, callback) => {
          const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
          const fileName = `${uniqueSuffix}${extname(file.originalname)}`;
          callback(null, fileName);
        },
      }),
    }),
  )
  @Put(':id')
  async update(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File,
    @Param('id') id: string,
    @Body() dto: Gass_ballonDto,
  ){
    if (!file) {
      throw new Error('Изображение обязательно');
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto: Gass_ballonDto = {
      ...dto,
      image: imagePath,
    };
    const user = (req as any).user;
    return this.gasBallonsSQLService.put(user,Number(id), updatedDto);
  }


  @ApiOperation({ summary: 'Фильтр газовых баллонов с пагинацией (SQL)' })
  @Get('get')
  async get(
    @Req() req: Request,
    @Query('volume') volume: string,
    @Query('page') page: string = '1',
    @Query('limit') limit: string = '10',
  ){
    const pageNumber = parseInt(page, 10);
    const pageSize = parseInt(limit, 10);

    const result = await this.gasBallonsSQLService.get(pageNumber, pageSize);

    return {
      total: result.total,
      page: pageNumber,
      limit: pageSize,
      data: result.data.map(ballon => ({
        ...ballon,
        image: `http://localhost:3000${ballon.image}`,
      })),
    };
  }
}
