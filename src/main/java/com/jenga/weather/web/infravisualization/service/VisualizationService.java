package com.jenga.weather.web.infravisualization.service;

import com.jenga.weather.domain.base.AWSEntity;
import com.jenga.weather.domain.ec2.model.EC2;
import com.jenga.weather.domain.ec2.service.EC2Service;
import com.jenga.weather.domain.elb.model.ELB;
import com.jenga.weather.domain.elb.service.ELBService;
import com.jenga.weather.domain.igw.model.IGW;
import com.jenga.weather.domain.igw.service.IGWService;
import com.jenga.weather.domain.nat.model.NAT;
import com.jenga.weather.domain.nat.service.NATService;
import com.jenga.weather.domain.rds.model.RDS;
import com.jenga.weather.domain.rds.service.RDSService;
import com.jenga.weather.domain.routeTable.service.RouteTableService;
import com.jenga.weather.domain.s3.model.S3;
import com.jenga.weather.domain.s3.service.S3Service;
import com.jenga.weather.domain.subnet.model.Subnet;
import com.jenga.weather.domain.subnet.service.SubnetService;
import com.jenga.weather.domain.vpc.model.VPC;
import com.jenga.weather.domain.vpc.service.VPCService;
import com.jenga.weather.web.infravisualization.node.Graph;
import com.jenga.weather.web.infravisualization.node.LeafNode;
import com.jenga.weather.web.infravisualization.node.Link;
import com.jenga.weather.web.infravisualization.node.RootNode;
import com.jenga.weather.web.infravisualization.node.dto.Ec2Node;
import com.jenga.weather.web.infravisualization.node.dto.ElbNode;
import com.jenga.weather.web.infravisualization.node.dto.EntryNode;
import com.jenga.weather.web.infravisualization.node.dto.IgwNode;
import com.jenga.weather.web.infravisualization.node.dto.NatNode;
import com.jenga.weather.web.infravisualization.node.dto.RdsNode;
import com.jenga.weather.web.infravisualization.node.dto.S3Node;
import com.jenga.weather.web.infravisualization.node.dto.SubnetNode;
import com.jenga.weather.web.infravisualization.node.dto.VpcNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Route;
import software.amazon.awssdk.services.ec2.model.RouteTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VisualizationService {

    private final EC2Service ec2Service;
    private final ELBService elbService;
    private final IGWService igwService;
    private final NATService natService;
    private final RDSService rdsService;
    private final S3Service s3Service;
    private final SubnetService subnetService;
    private final VPCService vpcService;
    private final RouteTableService routeTableService;

    public String mapInfra() {
        Ec2Client ec2Client = Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Infra Resource 목록 조회
        List<EC2> ec2List = ec2Service.getEC2Instances(ec2Client);
        List<ELB> elbList = elbService.getELBInstances();
        List<IGW> igwList = igwService.getIGWInstances(ec2Client);
        List<NAT> natList = natService.getNATInstances(ec2Client);
        List<RDS> rdsList = rdsService.getRDSInstances();
        List<S3> s3List = s3Service.getS3Instances();
        List<Subnet> subnetList = subnetService.getSubnetInstances(ec2Client);
        List<VPC> vpcList = vpcService.getVPCInstances(ec2Client);

        // Subnet 별 매핑
        HashMap<Subnet, List<AWSEntity>> subnetMapping = new HashMap<>();
        subnetList.forEach(subnet -> {
            List<AWSEntity> entities = new ArrayList<>();
            ec2List.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            natList.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            rdsList.forEach(vv -> {
                if (vv.getSubnetId().equals(subnet.getResourceId()))
                    entities.add(vv);
            });
            subnetMapping.put(subnet, entities);
        });

        // VPC 별 매핑
        HashMap<VPC, List<Subnet>> vpcMapping = new HashMap<>();
        vpcList.forEach(v -> {
            List<Subnet> subnets = new ArrayList<>();
            subnetMapping.forEach((k, vv) -> {
                if (v.getResourceId().equals(k.getVpcId())) {
                    subnets.add(k);
                }
                vpcMapping.put(v, subnets);
            });
        });

        // Infra 관계 생성
        StringBuffer result = new StringBuffer();
        result.append("VPC-Subnet 별 Resource\n");
        result.append("--------------------------------------------------\n");
        vpcMapping.forEach((vpc, subnets) -> {
            result.append("VPC : ").append(vpc).append("\n");
            subnets.forEach(subnet -> {
                result.append("    |Subnet : ").append(subnet).append("\n");
                subnetMapping.get(subnet).forEach(entity ->
                        result.append("        |").append(entity.toString()).append("\n")
                );
                elbList.forEach(entity -> {
                    if (entity.getVpcId().equals(vpc.getResourceId()))
                        result.append("    |").append(entity).append("\n");
                });
                igwList.forEach(entity -> {
                    if (entity.getVpcId().equals(vpc.getResourceId()))
                        result.append("        |").append(entity).append("\n");
                });
                result.append("\n");
            });
            result.append("\n");
            System.out.println();
        });
        result.append("S3-------------------------------------------------\n");
        s3List.forEach(entity ->
                result.append(entity.toString()).append("\n")
        );

        return result.toString();
    }

    public Graph getData() {
        int subnetIdx = 0;
        int ec2Idx = 0;
        int natIdx = 0;

        Graph graph = new Graph();
        RootNode rootNode = new RootNode();

        Ec2Client ec2Client = Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Infra Resource 목록 조회
        List<EC2> ec2List = ec2Service.getEC2Instances(ec2Client);
        List<ELB> elbList = elbService.getELBInstances();
        List<IGW> igwList = igwService.getIGWInstances(ec2Client);
        List<NAT> natList = natService.getNATInstances(ec2Client);
        List<Subnet> subnetList = subnetService.getSubnetInstances(ec2Client);
        List<RouteTable> routeTableList = routeTableService.getRouteTableInstances(ec2Client);

        List<RdsNode> rdsList = new ArrayList<>();
        for (RDS rds : rdsService.getRDSInstances()) {
            int rds_idx = 0;
            for (software.amazon.awssdk.services.rds.model.Subnet sub : rds.getSubnetGroups()) {
                RdsNode rdsNode = RdsNode.builder()
                        .id(rds.getResourceId() + '@' + rds_idx)
                        .name(rds.getResourceName())
                        .href("/img/aws_rds.png")
                        .engine(rds.getEngine())
                        .vpc_id(rds.getVpcId())
                        .subnet_id(sub.subnetIdentifier())
                        .status(rds.getDbInstanceStatus())
                        .db_instance_class(rds.getDbInstanceClass())
                        .region(rds.getRegion())
                        .build();

                rdsNode.addChildren(new LeafNode());
                rdsList.add(rdsNode);
                rds_idx += 1;
            }

            for (int i = 0; i < rds_idx - 1; i++) {
                graph.addLink(new Link(rds.getResourceId() + "@" + i,
                        rds.getResourceId() + "@" + (i + 1)));
            }
        }

        for (S3 s3 : s3Service.getS3Instances()) {
            S3Node s3Node = S3Node.builder()
                    .id("s3-" + s3.getResourceName())
                    .name(s3.getResourceName())
                    .href("/img/aws_s3.png")
                    .build();
            s3Node.addChildren(new LeafNode());

            rootNode.addChildren(s3Node);
        }

        int vpcIdx = 0;
        int igwIdx = 0;
        for (VPC vpc : vpcService.getVPCInstances(ec2Client)) {
            VpcNode vpcNode = VpcNode.builder()
                    .id(vpc.getResourceId())
                    .name("vpc-" + vpcIdx)
                    .resource_name(vpc.getResourceName())
                    .size(5)
                    .href("/img/aws_vpc.png")
                    .build();
            vpcNode.addChildren(new LeafNode());

            List<IGW> igws = igwList.stream()
                    .filter(i -> i.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            IGW igw = igws.get(0);

            IgwNode igwNode = IgwNode.builder()
                    .id(igw.getResourceId())
                    .name("igw-" + vpcIdx + "-" + igwIdx)
                    .owner_id(igw.getOwnerId())
                    .size(4)
                    .href("/img/aws_igw.png")
                    .build();
            igwNode.addChildren(new LeafNode());

            vpcNode.addChildren(igwNode);

            List<ELB> elbs = elbList.stream()
                    .filter(e -> e.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            for (ELB elb : elbs) {
                ElbNode elbNode = ElbNode.builder()
                        .id("elb-" + vpcNode.getId())
                        .name("elb-" + elb.getResourceName())
                        .href("/img/aws_elb.png")
                        .state(elb.getState())
                        .vpc_id(elb.getVpcId())
                        .resource_name(elb.getResourceName())
                        .build();

                elbNode.addChildren(new LeafNode());

                graph.addLink(new Link(elbNode.getId(), igwNode.getId()));
                vpcNode.addChildren(elbNode);
            }

            List<Subnet> subnets = subnetList.stream()
                    .filter(s -> s.getVpcId().equals(vpc.getResourceId()))
                    .collect(Collectors.toList());

            for (Subnet subnet : subnets) {
                SubnetNode subnetNode = SubnetNode.builder()
                        .id(subnet.getResourceId())
                        .name("subnet-" + vpcIdx + "-" + subnetIdx)
                        .size(4)
                        .resource_name(subnet.getResourceName())
                        .vpc_id(subnet.getVpcId())
                        .build();
                subnetNode.addChildren(new LeafNode());

                List<NAT> nats = natList.stream()
                        .filter(n -> n.getVpcId().equals(vpc.getResourceId())
                                && n.getSubnetId().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (NAT nat : nats) {
                    NatNode natNode = NatNode.builder()
                            .id(nat.getResourceId())
                            .name("nat-" + vpcIdx + "-" + subnetIdx + "-" + natIdx)
                            .href("/img/aws_nat.png")
                            .private_ip(nat.getPrivateIP())
                            .public_ip(nat.getPublicIP())
                            .vpc_id(nat.getVpcId())
                            .subnet_id(nat.getSubnetId())
                            .build();
                    natNode.addChildren(new LeafNode());

                    subnetNode.addChildren(natNode);
                    natIdx += 1;
                }
                natIdx = 0;

                List<RouteTable> routeTables = routeTableList.stream()
                        .filter(r -> r.vpcId().equals(vpc.getResourceId()) &&
                                (r.associations().get(0).subnetId() != null) &&
                                r.associations().get(0).subnetId().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                subnetNode.setRoute_table(new ArrayList<>());
                if (routeTables.size() >= 1) {
                    List<Route> routes = routeTables.get(0).routes();

                    for (Route route : routes) {
                        EntryNode entryNode = EntryNode.builder()
                                .destination_cidr_block(route.destinationCidrBlock())
                                .state(String.valueOf(route.state()))
                                .build();

                        if (entryNode.getDestination_cidr_block() != null && entryNode.getDestination_cidr_block().equals("0.0.0.0/0")) {
                            if (route.natGatewayId() != null) {
                                entryNode.setNat_gateway_id(route.natGatewayId());
                                subnetNode.setType("private");
                                graph.addLink(new Link(route.natGatewayId(), subnetNode.getId()));
                            } else if (route.gatewayId() != null && route.gatewayId().startsWith("igw-")) {
                                entryNode.setGateway_id(route.gatewayId());
                                subnetNode.setType("public");
                                graph.addLink(new Link(route.gatewayId(), subnetNode.getId()));
                            } else if (route.instanceId() != null) {
                                entryNode.setInstance_id(route.instanceId());
                                subnetNode.setType("private");
                                graph.addLink(new Link(route.instanceId(), subnetNode.getId()));
                            } else if (route.networkInterfaceId() != null) {
                                entryNode.setNetwork_interface_id(route.networkInterfaceId());
                                subnetNode.setType("blackhole");
                            }
                        } else {
                            entryNode.setGateway_id(route.gatewayId());
                        }
                        subnetNode.addRouteTable(entryNode);
                    }
                }
                else {
                    subnetNode.setType("empty");
                }
                subnetNode.setHref("/img/aws_" + subnetNode.getType() +"_subnet.png");

                List<RdsNode> rdsNodes = rdsList.stream()
                        .filter(r -> r.getVpc_id().equals(vpc.getResourceId())
                                && r.getSubnet_id().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (RdsNode rdsNode : rdsNodes) {
                    rdsNode.setName("rds-" + vpcIdx + "-" + subnetIdx);
                    subnetNode.addChildren(rdsNode);
                }

                List<EC2> ec2s = ec2List.stream()
                        .filter(e -> e.getSubnetId().equals(subnet.getResourceId()))
                        .collect(Collectors.toList());

                for (EC2 ec2 : ec2s) {
                    Ec2Node ec2Node = Ec2Node.builder()
                            .id(ec2.getResourceId())
                            .name("ec2-" + vpcIdx + "-" + subnetIdx + "-" + ec2Idx)
                            .size(3)
                            .href("/img/aws_ec2.png")
                            .type(ec2.getInstanceType())
                            .private_dns_name(ec2.getPrivateDnsName())
                            .private_ip_address(ec2.getPrivateIpAddress())
                            .image_id(ec2.getImageId())
                            .key_name(ec2.getKeyName())
                            .subnet_id(ec2.getSubnetId())
                            .vpc_id(ec2.getVpcId())
                            .resource_name(ec2.getResourceName())
                            .build();
                    ec2Node.addChildren(new LeafNode());

                    subnetNode.addChildren(ec2Node);
                    ec2Idx += 1;
                }
                ec2Idx = 0;
                vpcNode.addChildren(subnetNode);
                subnetIdx += 1;
            }
            subnetIdx = 0;
            rootNode.addChildren(vpcNode);
            vpcIdx += 1;
        }

        graph.setRoot(rootNode);

        return graph;
    }
}
