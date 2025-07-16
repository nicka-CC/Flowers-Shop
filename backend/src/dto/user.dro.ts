// dto/update-user.dto.ts
import { IsOptional, IsString, IsPhoneNumber, IsNotEmpty } from "class-validator";
import { ApiProperty } from "@nestjs/swagger";

export class UpdateUserDto {
  @ApiProperty({
    type: 'string',
    format: 'binary',
    description: 'image balloon'
  })
  @IsNotEmpty()
  face: string;
  @IsOptional()
  @IsString()
  @ApiProperty({example:'golder', description:'gas golder'})
  name?: string;

  @IsOptional()
  @IsString()
  @ApiProperty({example:'golder', description:'gas golder'})
  surname?: string;

  @IsOptional()
  @ApiProperty({example:'golder', description:'gas golder'})
  @IsPhoneNumber('RU') // или 'ZZ' если без проверки страны
  phone?: string;

}
