variable "ncp_access_key" {
  type = string
  description = "ncp access key id"
}

variable "ncp_secret_key" {
  type = string
  description = "ncp secret key"
}

variable "server_image_product_code" {
  type = string
  description = "ncp server image product code"
  default = "SW.VSVR.OS.LNX64.UBNTU.SVR2004.B050" // ubuntu-20.04
  // https://github.com/NaverCloudPlatform/terraform-ncloud-docs/blob/main/docs/server_image_product.md
}

variable "server_product_code" {
  type = string
  description = "ncp server product code"
  default = "SVR.VSVR.HICPU.C002.M004.NET.SSD.B050.G002" // vCPU 2EA, Memory 4GB, [SSD]Disk 50GB
  // https://github.com/NaverCloudPlatform/terraform-ncloud-docs/blob/main/docs/vpc_products/ubuntu-20.04.md
}
