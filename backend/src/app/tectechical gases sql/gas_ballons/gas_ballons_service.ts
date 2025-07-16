import { Inject, Injectable, UnauthorizedException } from "@nestjs/common";
import {Pool} from 'pg';
import { Gass_ballonDto } from "../../../dto/techical_gass/gass_ballon.dto";

@Injectable()
export class GasBallonsSQLService {
  constructor(@Inject('PG_POOL') private pool: Pool) {}
  async post(user,dto:Gass_ballonDto){

    if (user.permission !< 2){
      throw new UnauthorizedException('You haven`t privileges admin users');
    }
    const query = `
      INSERT INTO technical_gass (image, name, volume, description, date_created, date_updated)
      VALUES ($1, $2, $3, $4, NOW(), NOW())
      RETURNING *;
    `;
    const values = [dto.image, dto.name, dto.volume, dto.description];

    const { rows } = await this.pool.query(query, values);
    return rows[0]; // Возвращаем созданную запись
  }

  async put(user, gasBallonId: number, dto: Gass_ballonDto) {
    if (user.permission < 2) {
      throw new UnauthorizedException("You haven`t privileges admin users");
    }

    const query = `
      UPDATE technical_gass
      SET image = $1, name = $2, volume = $3, description = $4, date_updated = NOW()
      WHERE _id = $5
      RETURNING *;
    `;
    const values = [dto.image, dto.name, dto.volume, dto.description, gasBallonId];

    const { rows } = await this.pool.query(query, values);
    return rows[0];
  }

  async get( pageNumber: number, pageSize: number) {
    const query = `
        SELECT *
        FROM technical_gass LIMIT $1 OFFSET $2;
    `;

    const offset = (pageNumber - 1) * pageSize; // Рассчитываем смещение для пагинации
    const values = [ pageSize, offset]; // Передаем значения для запроса

    const { rows } = await this.pool.query(query, values);
    return rows;
  }
}