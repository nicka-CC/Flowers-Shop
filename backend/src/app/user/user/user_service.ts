import { Injectable, InternalServerErrorException, NotFoundException, UnauthorizedException } from "@nestjs/common";
import { PrismaService } from '../../../../prisma/prisma/prisma.service';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";
import { Gass_ballon_commentsDto } from "../../../dto/techical_gass/gas_ballon_comments.dto"
import { AGZSDto, AGZSPhotoDto, ApplicationDto, MainPhotoGalaryDto } from "../../../dto/agzs.dto";
import { CommentsDto } from "../../../dto/comments.dto";
import { NewsDto } from "../../../dto/news.dto";
import { UpdateUserDto } from "../../../dto/user.dro";

@Injectable()
export class UserService {
  constructor(private prisma:PrismaService) {}
  async getUserInfo(userId: number) {
    const user = await this.prisma.user.findUnique({
      where: { id: userId },
      include: {
        cart: true,
        history_orders: true,
      },
    });

    if (!user) throw new NotFoundException('Пользователь не найден');

    // Не возвращаем пароль
    const { password, ...safeUser } = user;
    return safeUser;
  }

  async updateUser(userId: number, dto: UpdateUserDto) {
    const user = await this.prisma.user.update({
      where: { id: userId },
      data: {
        ...dto,
        date_updated: new Date().toISOString(),
      },
    });

    const { password, ...safeUser } = user;
    return safeUser;
  }

  async deleteUser(userId: number) {
    await this.prisma.user.delete({
      where: { id: userId },
    });

    return { message: 'Пользователь успешно удалён' };
  }
}