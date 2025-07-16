import { ApiProperty, ApiPropertyOptional } from "@nestjs/swagger";
import { IsNotEmpty, IsNumberString, IsOptional } from "class-validator";


export class AGZSDto{
  @ApiProperty({example:'name', description:'name'})
  @IsNotEmpty()
  name: string;
  @ApiProperty({example:'open | new | default', description:'status forKeeping'})
  status: string;

  @ApiProperty({example:'100', description:'street'})
  street: string
  @ApiProperty({example:100, description:'price'})
  price: number;
  @ApiProperty({example:100, description:'price in offer'})
  price_in_offer: number
  @ApiProperty({example:'100', description:'position1'})
  position1: string
  @ApiProperty({example:'100', description:'position2'})
  position2: string
  @ApiProperty({example:'100', description:'position3'})
  position3: string
  @ApiProperty({example:'100', description:'position3'})
  position4: string
}

export class AGZSPhotoDto{
  @ApiProperty({
    type: 'string',
    format: 'binary',
    description: 'image balloon'
  })
  @IsNotEmpty()
  photo: string;
}
export class FilterPhotoDto {
  @ApiPropertyOptional({ description: 'ID AGZS' })
  @IsOptional()
  @IsNumberString()
  agzsId?: number;

  @ApiPropertyOptional({ description: 'ID фото' })
  @IsOptional()
  @IsNumberString()
  photoId?: number;

  @ApiPropertyOptional({ default: '1' })
  @IsOptional()
  @IsNumberString()
  page?: string = '1';

  @ApiPropertyOptional({ default: '10' })
  @IsOptional()
  @IsNumberString()
  limit?: string = '10';
}
export class ApplicationDto{
  @ApiProperty({example:'name', description:'name'})
  @IsNotEmpty()
  name: string;
  @ApiProperty({example:'+79009926945', description:'phone number'})
  @IsNotEmpty()
  phone: string;
  @ApiProperty({example:'created | answer', description:'status status'})
  @IsNotEmpty()
  status: string
  @ApiProperty({example:'Help me, PLease', description:'text from client'})
  @IsNotEmpty()
  about_status: string;
  @ApiProperty({example:'agzs', description:'text page'})
  @IsNotEmpty()
  about_where: string

}
export class ApplicationOnKeepDto{
  @ApiProperty({example:'500', description:'volume'})
  @IsNotEmpty()
  volume: string;
  @ApiProperty({example:'+79009926945', description:'phone number'})
  @IsNotEmpty()
  phone: string;
  @ApiProperty({example:'created | answer', description:'status status'})
  @IsNotEmpty()
  status: string
  @ApiProperty({example:'Help me, PLease', description:'text from client'})
  @IsNotEmpty()
  description_status: string;
  @ApiProperty({example:'19.06.1982-1.1.1999', description:'period'})
  @IsNotEmpty()
  period: string

}
export class ApplicationOnDeliveryDto{
  @ApiProperty({example:'500', description:'volume'})
  name: string;
  @ApiProperty({example:'+79009926945', description:'phone number'})
  phone: string;
  @ApiProperty({example:'created | answer', description:'status status'})
  status: string
  @ApiProperty({example:'created | answer', description:'status status'})
  address: string
  @ApiProperty({example:'[]', description:'status status'})
  option: string
  @ApiProperty({example:'Help me, PLease', description:'text from client'})
  description_status: string;
}
export class ApplicationOnCalculateDto{
  @ApiProperty({example:'500', description:'volume'})
  name: string;
  @ApiProperty({example:'+79009926945', description:'phone number'})
  phone: string;
  @ApiProperty({example:'created | answer', description:'status status'})
  status: string
  @ApiProperty({example:'created | answer', description:'status status'})
  address: string
  @ApiProperty({example:'[]', description:'variant'})
  variant: string
  @ApiProperty({example:'11.11.2911', description:'date'})
  date: string
  @ApiProperty({example:0, description:'total'})
  total: number
  @ApiProperty({example:'Help me, PLease', description:'text from client'})
  description_status: string;
}
export class MainPhotoGalaryDto{
  @ApiProperty({
    type: 'string',
    format: 'binary',
    description: 'image balloon'
  })
  @IsNotEmpty()
  image: string;
  @ApiProperty({example:'agzs', description:'module page'})
  @IsNotEmpty()
  module: string
}