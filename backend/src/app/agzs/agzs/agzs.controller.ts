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
import {
  AGZSDto,
  AGZSPhotoDto,
  ApplicationDto, ApplicationOnCalculateDto, ApplicationOnDeliveryDto,
  ApplicationOnKeepDto,
  FilterPhotoDto,
  MainPhotoGalaryDto,
} from "../../../dto/agzs.dto";
import { AGZSService} from "./agzs_service";
import { FileInterceptor } from "@nestjs/platform-express";
import { diskStorage } from "multer";
import { extname } from "node:path";
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto";
import { IsOptional } from "class-validator";

@ApiTags("AGZS")
@ApiBearerAuth()
@Controller("agzs")
export class AGZSController {
  constructor(private gasBallonsService: AGZSService) {
  }

  @UseGuards(JwtAuthGuard)
  @Post("register")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  async register(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: AGZSDto, // DTO для данных
  ) {

    const updatedDto: AGZSDto = dto;
    const user = (req as any).user;
    return this.gasBallonsService.post(user, updatedDto);
  };

  @UseGuards(JwtAuthGuard)
  @ApiOperation({ summary: "Газовый баллон успешно обновлён" })

  @Put(":id")
  async update(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File,
    @Param("id") id: string,
    @Body() dto: AGZSDto,
  ) {

    const updatedDto: AGZSDto = dto;
    const user = (req as any).user;
    return this.gasBallonsService.put(user, Number(id), updatedDto);
  }


  @Get()
  @ApiOperation({ summary: "Газовый баллоны успешно получены" })
  async filter(
    @Req() req: Request,
    @Query('status') status: string,
    @Query('page') page: string = '1',
    @Query('limit') limit: string = '10',
  ) {
    const pageNumber = parseInt(page, 10);
    const pageSize = parseInt(limit, 10);

    const result = await this.gasBallonsService.get(status, pageNumber, pageSize);

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
  @UseGuards(JwtAuthGuard)
  @Post("photogallary/register/:agzsId")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  @ApiConsumes("multipart/form-data")
  @UseInterceptors(
    FileInterceptor("photo", {
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
  async registerPhoto(
    @Req() req: Request,
    @Param("agzsId") id: string,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: AGZSPhotoDto, // DTO для данных
  ) {
    if (!file) {
      throw new Error("Изображение обязательно");
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto: AGZSPhotoDto = {
      ...dto,
      photo: imagePath,
    };
    const user = (req as any).user;
    return this.gasBallonsService.postPhoto(user, updatedDto, Number(id));
  };

  @Get("photogallary/")
  @ApiOperation({ summary: "Газовый баллоны успешно получены" })
  @IsOptional()
  async filterPhoto(
    @Req() req: Request,
    @Query() query: FilterPhotoDto
  ) {
    const pageNumber = parseInt(query.page ?? '1', 10);
    const pageSize = parseInt(query.limit ?? '10', 10);

    const result = await this.gasBallonsService.getPhoto( query.agzsId,
      query.photoId, pageNumber, pageSize);

    return {
      total: result.total,
      page: pageNumber,
      limit: pageSize,
      data: result.data.map(ballon => ({
        ...ballon,
        full_image: `http://localhost:7000${ballon.image}`,
      })),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Delete('photogallary/photo/:id')
  async deletePhoto(
    @Req() req: Request,
    @Param("id") id: string,) {
    const user = (req as any).user;
    return this.gasBallonsService.deletePhoto(user, Number(id));
  }

}

@ApiTags("Application")
@ApiBearerAuth()
@Controller("application")
export class ApplicationController {
  constructor(private gasBallonsService: AGZSService) {
  }

  @Post("register")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  async register(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: ApplicationDto, // DTO для данных
  ) {

    const updatedDto: ApplicationDto = dto;
    return this.gasBallonsService.postApplication(updatedDto);
  };
  @Post("on_keep")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  async registerOnKeep(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: ApplicationOnKeepDto, // DTO для данных
  ) {

    const updatedDto: ApplicationOnKeepDto = dto;
    return this.gasBallonsService.postApplicationonKeep(updatedDto);
  };
  @Post("on_delivery")
  @ApiOperation({ summary: "Газовый баллон успешно создан" })
  async registerOnDelivery(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: ApplicationOnDeliveryDto, // DTO для данных
  ) {

    const updatedDto: ApplicationOnDeliveryDto = dto;
    return this.gasBallonsService.postApplicationonDelivery(updatedDto);
  };
  @Post("on_option")
  @ApiOperation({ summary: "Выберите вариант обслуживания-установки-выбор услуги-подбор под ваши подробности" })
  async registerOnServiceOption(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: ApplicationOnDeliveryDto, // DTO для данных
  ) {

    const updatedDto: ApplicationOnDeliveryDto = dto;
    return this.gasBallonsService.postApplicationonOption(updatedDto);
  };
  @Post("on_calculate")
  @ApiOperation({ summary: "Расчет расхода баллона" })
  async registerOnCalculateBallone(
    @Req() req: Request,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: ApplicationOnCalculateDto, // DTO для данных
  ) {

    const updatedDto: ApplicationOnCalculateDto = dto;
    return this.gasBallonsService.postApplicationonCalculate(updatedDto);
  };
}

@ApiTags("Photogallary")
@ApiBearerAuth()
@Controller("photogallary")
export class MainPhotoGallaryController {
  constructor(private gasBallonsService: AGZSService) {
  }


  @UseGuards(JwtAuthGuard)
  @Post("photogallary_global/register/:agzsId")
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
  async registerPhoto(
    @Req() req: Request,
    @Param("agzsId") id: string,
    @UploadedFile() file: Express.Multer.File, // Тип для файла
    @Body() dto: MainPhotoGalaryDto, // DTO для данных
  ) {
    if (!file) {
      throw new Error("Изображение обязательно");
    }
    const imagePath = `/uploads/${file.filename}`;
    const updatedDto: MainPhotoGalaryDto = {
      ...dto,
      image: imagePath,
    };
    const user = (req as any).user;
    return this.gasBallonsService.postPhotoGallary(user, updatedDto, Number(id));
  };

  @Get("photogallary_global/")
  @ApiOperation({ summary: "Газовый баллоны успешно получены" })
  @IsOptional()
  async filterPhoto(
    @Req() req: Request,
    @Query('module') module: string,
    @Query('page') page: string = '1',
    @Query('limit') limit: string = '10',
  ) {

    const result = await this.gasBallonsService.getPhotoGallary( module,Number( page), Number(limit));

    return {
      total: result.total,
      page: page,
      limit: limit,
      data: result.data.map(ballon => ({
        ...ballon,
        full_image: `http://localhost:7000${ballon.image}`,
      })),
    };
  }

  @UseGuards(JwtAuthGuard)
  @Delete('photogallary_global/photo/:id')
  async deletePhoto(
    @Req() req: Request,
    @Param("id") id: string,) {
    const user = (req as any).user;
    return this.gasBallonsService.deletePhotoGallary(user, Number(id));
  }


}
