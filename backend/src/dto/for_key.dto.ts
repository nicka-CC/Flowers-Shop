import { ApiProperty, ApiPropertyOptional } from "@nestjs/swagger";
import { IsNotEmpty, IsNumberString, IsOptional } from "class-validator";



export class ForKeyDto{
  @ApiProperty({
    type: 'string',
    format: 'binary',
    description: 'image balloon'
  })
  @IsNotEmpty()
  image: string;
  @ApiProperty({example:'golder', description:'gas golder'})
  @IsNotEmpty()
  gasGolder: string
  @ApiProperty({example:66, description:'liter'})
  @IsNotEmpty()
  liter: number
  @ApiProperty({example:1, description:'price'})
  @IsNotEmpty()
  price: number
}