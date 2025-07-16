import { ApiProperty, ApiPropertyOptional } from "@nestjs/swagger";
import { IsNotEmpty, IsNumberString, IsOptional } from "class-validator";



export class OffersDto{
  @ApiProperty({
    type: 'string',
    format: 'binary',
    description: 'image balloon'
  })
  @IsNotEmpty()
  image: string;
  @ApiProperty({example:'text text text...', description:'text offer'})
  @IsNotEmpty()
  condition: string
}
export class ForKeepingDto{

  @ApiProperty({example:'text text text...', description:'status'})
  @IsNotEmpty()
  status:string;
  @ApiProperty({example:'text text text...', description:'name'})
  @IsNotEmpty()
  name:string;
  @ApiProperty({example:'text text text...', description:'street'})
  @IsNotEmpty()
  street:string;
  @ApiProperty({example:100, description:'price'})
  @IsNotEmpty()
  price:number;
  @ApiProperty({example:90, description:'price %'})
  @IsNotEmpty()
  price_in_offer:number;
  @ApiProperty({example:'text text text...', description:'location'})
  @IsNotEmpty()
  position1:string;
  @ApiProperty({example:'text text text...', description:'location'})
  @IsNotEmpty()
  position2:string;
  @ApiProperty({example:'text text text...', description:'location'})
  @IsNotEmpty()
  position3:string;
  @ApiProperty({example:'text text text...', description:'location'})
  @IsNotEmpty()
  position4:string;
}