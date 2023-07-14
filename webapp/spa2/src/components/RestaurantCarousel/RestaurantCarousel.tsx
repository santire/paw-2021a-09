import { Group, Loader, Text, Title, Tooltip } from "@mantine/core";
import { Carousel } from "@mantine/carousel";
import { IRestaurant } from "../../types/restaurant/restaurant.models";
import { Center, createStyles } from "@mantine/core";
import { RestaurantCard } from "../RestaurantCard/RestaurantCard";
import { useTranslation } from "react-i18next";
import { IconInfoCircle } from "@tabler/icons-react";

interface RestaurantCarouselProps {
  title: string;
  tooltip: string;
  restaurants?: IRestaurant[];
  loading: boolean;
}

const useStyles = createStyles((theme, _params, getRef) => ({
  title: {
    fontSize: theme.fontSizes.xl * 1.8,
    paddingLeft: theme.spacing.sm,
  },

  indicator: {
    ref: getRef("indicator"),
    position: "relative",
    top: 40,
    transition: "opacity 150ms ease",
    opacity: 0,
    "&[data-active]": {
      opacity: 0,
    },
  },
  controls: {
    ref: getRef("controls"),
    transition: "opacity 150ms ease",
    opacity: 0,
  },

  root: {
    "&:hover": {
      [`& .${getRef("controls")}`]: {
        opacity: 1,
      },
      [`& .${getRef("indicator")}`]: {
        "&[data-active]": {
          opacity: 1,
        },
        opacity: 1,
      },
    },
  },
}));

export function RestaurantCarousel({
  title,
  tooltip,
  restaurants,
  loading,
}: RestaurantCarouselProps) {
  const { t } = useTranslation();

  if (loading) {
    return (
      <CarouselWrapper tooltip={tooltip} title={title}>
        <Center w={"100%"} mih={200}>
          <Loader variant="dots" color="orange" size={65} />
        </Center>
      </CarouselWrapper>
    );
  }

  if (!loading && restaurants && restaurants.length > 0) {
    // If exactly 5 restaurants looping breaks, so double array to fix loop
    if (restaurants.length == 5) {
      restaurants = [...restaurants, ...restaurants];
    }
    return (
      <CarouselWrapper
        title={title}
        tooltip={tooltip}
        withControls={restaurants && restaurants.length >= 5}
      >
        {restaurants.map((r, i) => (
          <Carousel.Slide key={r.id + "_" + i + title}>
            <Center p={0} m={0}>
              <RestaurantCard restaurant={r} />
            </Center>
          </Carousel.Slide>
        ))}
      </CarouselWrapper>
    );
  }

  return (
    <CarouselWrapper title={title} tooltip={tooltip}>
      <Center w={"100%"} mih={200}>
        <Text size={36} italic align="center" mt={30}>
          {t("restaurantCarousel.noRestaurants")}
        </Text>
      </Center>
    </CarouselWrapper>
  );
}

type WrapperProps = {
  title: string;
  tooltip: string;
  children: string | JSX.Element | JSX.Element[];
  withControls?: boolean;
};
function CarouselWrapper({
  title,
  tooltip,
  withControls,
  children,
}: WrapperProps) {
  const { classes } = useStyles();
  return (
    <>
      <Group>
        <Text className={classes.title}>{title}</Text>
        <Tooltip label={tooltip} position="right" withArrow>
          <Text color="dimmed" sx={{ cursor: "help" }}>
            <Center>
              <IconInfoCircle size="1.1rem" stroke={1.5} />
            </Center>
          </Text>
        </Tooltip>
      </Group>
      <Carousel
        mih={200}
        classNames={{
          root: classes.root,
          controls: classes.controls,
          indicator: classes.indicator,
        }}
        mx="auto"
        slideSize="20%"
        slideGap="sm"
        breakpoints={[
          { maxWidth: "lg", slideSize: "33.333333333%" },
          { maxWidth: "md", slideSize: "50%" },
          { maxWidth: "sm", slideSize: "100%", slideGap: 0 },
        ]}
        align="start"
        withControls={withControls}
        withIndicators={withControls}
        controlSize={40}
        loop={true}
      >
        {children}
      </Carousel>
    </>
  );
}
