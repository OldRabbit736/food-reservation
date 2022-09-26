provider "ncloud" {
  support_vpc = true
  region      = "KR"
  access_key  = var.ncp_access_key
  secret_key  = var.ncp_secret_key
}

locals {
  project_name = "food-reservation"
}

# VPC
resource "ncloud_vpc" "vpc" {
  ipv4_cidr_block = "10.0.0.0/16"
  name            = "vpc-${local.project_name}"
}

# Public Subnet
resource "ncloud_subnet" "subnet_public" {
  network_acl_no = ncloud_network_acl.acl_public.id
  subnet         = cidrsubnet(ncloud_vpc.vpc.ipv4_cidr_block, 8, 0)  // "10.0.0.0/24"
  subnet_type    = "PUBLIC"
  vpc_no         = ncloud_vpc.vpc.id
  zone           = "KR-1"
  name           = "subnet-${local.project_name}"
}

# Network ACL
resource "ncloud_network_acl" "acl_public" {
  vpc_no = ncloud_vpc.vpc.id
  name   = "acl-public-${local.project_name}"
}

# Login Key
resource "ncloud_login_key" "login_key" {
  key_name = local.project_name
}

# Server
resource "ncloud_server" "server_public" {
  subnet_no = ncloud_subnet.subnet_public.id
  name      = "server-${local.project_name}"
  server_image_product_code = var.server_image_product_code
  server_product_code = var.server_product_code
  login_key_name = ncloud_login_key.login_key.key_name
}

# Public IP
resource "ncloud_public_ip" "public_ip" {
  server_instance_no = ncloud_server.server_public.id
}

data "ncloud_root_password" "root_password" {
  server_instance_no = ncloud_server.server_public.id
  private_key = ncloud_login_key.login_key.private_key
}

// TODO - default ACG 포트 설정
